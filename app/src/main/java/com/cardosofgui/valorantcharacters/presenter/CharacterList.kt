package com.cardosofgui.valorantcharacters.presenter

import Datum
import android.content.Intent
import com.cardosofgui.valorantcharacters.R
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.cardosofgui.valorantcharacters.framework.viewmodel.AgentViewModel
import com.cardosofgui.valorantcharacters.presenter.ui.theme.ValorantCharactersTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import okhttp3.internal.filterList
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterList : ComponentActivity() {

    private val agentViewModel : AgentViewModel by viewModel()
    private lateinit var alata : FontFamily

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        agentViewModel.getAllAgents()
        initFontFamily()

        setContent {
            ValorantCharactersTheme() {
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color.Black,
                        darkIcons = false
                    )
                }

                allContent()
            }
        }
    }

    private fun initFontFamily() {
        alata = FontFamily(
            Font(R.font.alata)
        )
    }

    @Composable
    @Preview
    private fun allContent() {
        Scaffold(
            backgroundColor = Color(15, 24, 33, 235)
        ) {
            mainContent()
        }
    }

    @Composable
    private fun mainContent() {
        val showEditText = remember { mutableStateOf(false) }
        val agentEditText = remember { mutableStateOf("") }

        val agentList = remember { mutableStateOf(agentViewModel.agent) }
        val iconSearch = remember { mutableStateOf(Icons.Filled.Search) }

        Column() {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
                Image(
                    painter = painterResource(id = R.drawable.valorant_logo),
                    contentDescription = "",
                    modifier = Modifier
                        .width(84.dp)
                        .wrapContentHeight()
                        .weight(1f)
                )


                if(showEditText.value) {
                    OutlinedTextField(
                        value = agentEditText.value,
                        onValueChange = {
                            agentEditText.value = it

                            if(it.isEmpty()) agentList.value = agentViewModel.agent
                            else agentList.value = agentViewModel.agent.filter { agent -> agent!!.displayName!!.contains(it, true) }
                        },
                        label = { Text(
                            text = "Digite o nome de um Agente",
                            color = Color(255, 68, 87)
                        ) },
                        modifier = Modifier
                            .align(CenterVertically)
                            .weight(3f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(255, 68, 87),
                            unfocusedBorderColor = Color(255, 68, 87),
                            cursorColor = Color(255, 68, 87)
                        ),
                        textStyle = TextStyle(
                            color = Color(255, 68, 87)
                        )
                    )
                } else {
                    Text(
                        text = "Agentes",
                        modifier = Modifier
                            .align(CenterVertically)
                            .weight(3f),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(255, 68, 87)
                    )
                }

                IconButton(
                    onClick = {
                        showEditText.value = !showEditText.value

                        if(showEditText.value) iconSearch.value = Icons.Filled.Close
                        else {
                            agentList.value = agentViewModel.agent
                            agentEditText.value = ""
                            iconSearch.value = Icons.Filled.Search
                        }
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .size(32.dp)
                        .align(Alignment.CenterVertically)
                        .weight(.7f),

                    ) {
                    Icon(
                        imageVector = iconSearch.value,
                        contentDescription = "Back",
                        tint = Color(255, 68, 87),
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }

            Column(
                Modifier.fillMaxWidth()
            ) {
                LazyColumn() {
                    items(agentList.value) { agent ->
                        Card(
                            Modifier
                                .padding(bottom = 6.dp, top = 6.dp, start = 12.dp, end = 12.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(
                                    onClick = {
                                        changeActivity(
                                            CharacterInfo::class.java,
                                            agent!!
                                        )
                                    },
                                    indication = rememberRipple(true),
                                    interactionSource = remember { MutableInteractionSource() }
                                ),
                            backgroundColor = Color(255, 68, 87)
                        ) {
                            Row(
                                verticalAlignment = CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        agent!!.displayIcon
                                    ),
                                    contentDescription = "Person Image",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .heightIn(min = 120.dp)
                                )

                                Column(
                                    Modifier.padding(end = 12.dp)
                                ) {

                                    Text(
                                        text = agent.displayName!!,
                                        fontSize = 32.sp,
                                        fontFamily = alata,
                                        color = Color(15, 24, 33, 235),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )

                                    Text(
                                        text = agent.description!!,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        color = Color.White,
                                        fontFamily = alata,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    Modifier
                                        .align(Alignment.TopEnd)
                                ) {
                                    for (ability in agent!!.abilities!!) {
                                        if (ability.displayIcon != null) {
                                            Image(
                                                painter = rememberAsyncImagePainter(
                                                    ability.displayIcon
                                                ),
                                                contentDescription = "Ability Image",
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .padding(4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun <T : Any> changeActivity(destiny : Class<T>, person : Datum) {
        Intent(this, destiny).let {
            it.putExtra("PERSON_KEY", person)
            startActivity(it)
        }
    }
}
