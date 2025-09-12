package com.example.rasago

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.rasago.ui.theme.Baloo2
import com.example.rasago.ui.theme.DarkGreen
import com.example.rasago.ui.theme.GreyGreen
import com.example.rasago.ui.theme.LightGreen

@Composable
fun EditableProfilePicture(
    isEditMode: Boolean,
    selectedImage: Int,
    modifier: Modifier = Modifier,
    onChangeClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .clickable(enabled = isEditMode) { onChangeClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(selectedImage),
            contentDescription = stringResource(R.string.default_profile_picture),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay when editing
        if (isEditMode) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.change_pfp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = Baloo2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
@Composable
fun EditProfileScreen(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    //TODO: Temporary Default, need to read from database
    var selectedImage by remember { mutableStateOf(R.drawable.default_profile_picture) }

    var name by remember { mutableStateOf("Tharaka") }
    var mobile by remember { mutableStateOf("012-232221") }
    var email by remember { mutableStateOf("tharaka@gmail.com") }
    var dob by remember { mutableStateOf("25-12-2006") }
    var gender by remember { mutableStateOf("Female") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.background_image),
                contentDescription = stringResource(R.string.background),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 20.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.save),
                    contentDescription = stringResource(R.string.save),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.save),
                    fontSize = 16.sp,
                    fontFamily = Baloo2
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center), 
                contentAlignment = Alignment.Center
            ) {
                EditableProfilePicture(
                    isEditMode = true,
                    onChangeClick = { showDialog = true },
                    modifier = Modifier.align(Alignment.Center),
                    selectedImage = selectedImage
                )
            }
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreen)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.personal_information),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    fontFamily = Baloo2
                )

            }

        }
        Spacer(modifier = Modifier.height(4.dp))

        Column {
            EditableProfileItem(
                label = R.string.name,
                value = name,
                onValueChange = { name = it }
            )
            EditableProfileItem(
                label = R.string.mobile_number,
                value = mobile,
                onValueChange = { mobile = it }
            )
            EditableProfileItem(
                label = R.string.email,
                value = email,
                onValueChange = { email = it }
            )
            EditableProfileItem(
                label = R.string.dob,
                value = dob,
                onValueChange = { dob = it }
            )
            EditableProfileItem(
                label = R.string.gender,
                value = gender,
                onValueChange = { gender = it }
            )
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.change_pfp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = Baloo2
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val images = listOf(
                            R.drawable.default_profile_picture,
                            R.drawable.choice_profile_picture,
                            R.drawable.choice2_profile_picture
                        )
                        items(images.size) { index ->
                            Image(
                                painter = painterResource(id = images[index]),
                                contentDescription = "Profile Option $index",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        selectedImage = images[index]
                                        showDialog = false
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
                    ) {
                        Text("Cancel", color = Color.White, fontFamily = Baloo2)
                    }
                }
            }
        }
    }

}


@Composable
fun EditableProfileItem(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit
) {
    var textValue by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .background(color = LightGreen),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(label),
            color = DarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Baloo2,
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            maxLines = 3,          // allow up to 3 lines
            softWrap = true
        )

        TextField(
            value = textValue,
            onValueChange = {
                textValue = it
                onValueChange(it)
            },
            label = null,
            modifier = Modifier
                .weight(2f)
                .padding(start = 8.dp),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Baloo2
            ),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                // Background
                focusedContainerColor = Color.White,   // clean when editing
                unfocusedContainerColor = GreyGreen,   // soft green when idle
                disabledContainerColor = Color(0xFFE0E0E0), // greyed out when disabled

                // Underline
                focusedIndicatorColor = DarkGreen,     // dark green underline when active
                unfocusedIndicatorColor = Color.Transparent, // no underline when idle
                disabledIndicatorColor = Color.Transparent,

                cursorColor = DarkGreen,

                focusedLabelColor = DarkGreen,
                unfocusedLabelColor = LightGreen
            ),
        )
    }
    Spacer(modifier = Modifier.height(4.dp))

    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))

    Spacer(modifier = Modifier.height(4.dp))


}


@Preview(showBackground = true)
@Composable
fun PreviewEditScreen(){
    EditProfileScreen()
}