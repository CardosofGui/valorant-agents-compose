package com.cardosofgui.valorantcharacters.presenter

import Ability
import Datum
import android.graphics.Paint.Align
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.cardosofgui.valorantcharacters.R
import com.cardosofgui.valorantcharacters.presenter.ui.theme.ValorantCharactersTheme
import java.lang.Exception

class CharacterInfo : ComponentActivity() {

    private lateinit var agent : Datum
    private lateinit var alata : FontFamily
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        agent = intent.getSerializableExtra("PERSON_KEY") as Datum

        initFontFamily()

        setContent {
            allContent()
        }
    }

    private fun initFontFamily() {
        alata = FontFamily(
            Font(R.font.alata)
        )
    }

    @Composable
    private fun allContent() {
        Scaffold(
            backgroundColor = Color(15, 24, 33, 235)
        ) { }

        navigationBackIcon()

        personContent()
    }
    
    @Composable
    private fun navigationBackIcon() {
        IconButton(
            onClick = { onBackPressed() },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(255, 68, 87),
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun personContent() {
        val abilityScrollHorizontal = rememberScrollState(0)
        val openDialogAbility = remember { mutableStateOf(false) }
        val selectAbility = remember { mutableStateOf(Ability()) }

        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberAsyncImagePainter(agent.fullPortraitV2),
                contentDescription = "Agent Image",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            if(openDialogAbility.value) {
                Dialog(
                    onDismissRequest = {
                        openDialogAbility.value = false
                    },
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = Color(255, 68, 87),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = selectAbility.value.displayIcon),
                                contentDescription = "Abilitiy Image",
                                modifier = Modifier.size(104.dp)
                            )

                            Text(
                                text = selectAbility.value.displayName ?: "Error",
                                fontSize = 32.sp,
                                fontFamily = alata,
                                color = Color(15, 24, 33, 235),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = selectAbility.value.description ?: "Error",
                                color = Color.White,
                                fontFamily = alata,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start
                            )
                        }

                    }
                }
            }

            Card(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp)),
                backgroundColor = Color(255, 68, 87)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                    ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = agent.displayName ?: "Error",
                            fontSize = 32.sp,
                            fontFamily = alata,
                            color = Color(15, 24, 33, 235),
                            fontWeight = FontWeight.Bold,
                        )

                        Image(
                            painter = rememberAsyncImagePainter(model = R.drawable.ic_baseline_volume_up_24),
                            contentDescription = "Play Audio",
                            Modifier
                                .size(24.dp)
                                .clickable(
                                    onClick = { playAudio() },
                                    indication = rememberRipple(true),
                                    interactionSource = remember { MutableInteractionSource() }
                                )
                        )
                    }

                    Text(
                        text = agent.description!!,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        fontFamily = alata,
                        fontSize = 14.sp,
                    )

                    Row(
                        Modifier
                            .horizontalScroll(abilityScrollHorizontal)
                    ) {
                        for (ability in agent.abilities ?: return@Row) {
                            if (ability.displayIcon != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        ability.displayIcon
                                    ),
                                    contentDescription = "Ability Image",
                                    modifier = Modifier
                                        .size(84.dp)
                                        .padding(4.dp)
                                        .clickable(
                                            onClick = {
                                                selectAbility.value = ability
                                                openDialogAbility.value = true
                                            },
                                            indication = rememberRipple(true),
                                            interactionSource = remember { MutableInteractionSource() }
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun playAudio() {
        if (mediaPlayer?.isPlaying() == true)  {
            mediaPlayer!!.stop();
            mediaPlayer!!.release();
        }

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)

        try {
            mediaPlayer!!.setDataSource(agent.voiceLine?.mediaList?.get(0)!!.wave)
            mediaPlayer!!.prepare();
            mediaPlayer!!.start();

        } catch (e : Exception) {
            Log.e("Error Audio", "$e")
        }
    }
}
