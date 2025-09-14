//package com.example.rasago.theme.profile
//
//import androidx.annotation.StringRes
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.rasago.R
//import com.example.rasago.ui.theme.Baloo2
//import com.example.rasago.ui.theme.DarkGreen
//import com.example.rasago.ui.theme.LightGreen
//
//
//enum class AccountStatus(val label: String, val color: Color) {
//    Active("Active", Color(0xFF4CAF50)),              // Green
//    Inactive("Inactive", Color(0xFF9E9E9E)),          // Grey
//    Suspended("Suspended", Color(0xFFFF9800)),        // Orange
//    Banned("Banned", Color(0xFFF44336)),              // Red
//    Pending("Pending Verification", Color(0xFF2196F3)) // Blue
//}
//
//@Composable
//fun AccountStatusBadge(status: AccountStatus) {
//    Box(
//        modifier = Modifier
//            .background(color = status.color, shape = RoundedCornerShape(16.dp))
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//    ) {
//        Text(
//            text = status.label,
//            color = Color.White
//        )
//    }
//}
//
//
//
//@Composable
//fun AccountStatusScreen() {
//    // TODO: Temporary put as active
//    var status by remember { mutableStateOf(AccountStatus.Active) }
//
//    Column(
//        modifier = Modifier.padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        // Status Badge
//        AccountStatusBadge(status)
//
//        // TODO: For Staff only
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            Button(onClick = { status = AccountStatus.Active }) {
//                Text("Set Active")
//            }
//            Button(onClick = { status = AccountStatus.Suspended }) {
//                Text("Suspend")
//            }
//            Button(onClick = { status = AccountStatus.Pending }) {
//                Text("Pending")
//            }
//        }
//    }
//}
//
//@Composable
//fun ProfileScreen(){
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ){
//
//        Box(
//            modifier = Modifier
//            .fillMaxWidth().height(160.dp)
//        ){
//            Column(modifier = Modifier.align(alignment = Alignment.TopEnd)) {
//                Image(
//                    painter = painterResource(R.drawable.background_image),
//                    contentDescription = stringResource(R.string.background),
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )
//
//            }
//            Column(modifier = Modifier.align(Alignment.TopEnd).padding(top = 16.dp, end = 16.dp)) {
//                Icon(
//                    imageVector = Icons.Default.Edit,
//                    contentDescription = stringResource(R.string.edit),
//                    modifier = Modifier
//                        .clickable {
//                            //TODO: Click the profile picture and select profile picture
//                        }
//                        .size(32.dp)
//
//                )
//                Text(
//                    text = stringResource(R.string.edit),
//                    fontSize = 16.sp,
//                    fontFamily = Baloo2,
//                    modifier = Modifier.padding(end = 16.dp)
//                )
//            }
//            Image(
//                painter = painterResource(id = R.drawable.default_profile_picture),
//                contentDescription = stringResource(R.string.default_profile_picture),
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .align(Alignment.Center),
//
//                contentScale = ContentScale.Crop
//            )
//        }
//        Card(modifier = Modifier.fillMaxWidth().padding(16.dp),
//            shape = RoundedCornerShape(8.dp),
//            colors = CardDefaults.cardColors(LightGreen)
//        ) {
//            Column (modifier = Modifier.padding(16.dp)){
//                Text(
//                    text = stringResource(id = R.string.personal_information),
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = DarkGreen,
//                    fontFamily = Baloo2
//                )
//            }
//        }
//        // TODO: Need to relate to database and pass data
//        // TODO: Edit there need validation
//        ProfileItem(label = R.string.name, value = "Testing")
//        ProfileStatus(label = R.string.status)
//        ProfileItem(label = R.string.mobile_number, value = "012-2322323")
//        ProfileItem(label = R.string.email, value = "testing@gmail.com")
//        ProfileItem(label = R.string.dob, value = "25-12-2006")
//        ProfileItem(label = R.string.gender, value = "Female")
//
//    }
//}
//
//@Composable
//fun ProfileItem(@StringRes label:Int , value: String){
//    Row (
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ){
//        Text(
//            text = stringResource(label),
//            color = DarkGreen,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Medium,
//            modifier = Modifier.align(alignment = Alignment.CenterVertically),
//            fontFamily = Baloo2
//        )
//            Text(
//                text = value, fontWeight = FontWeight.Medium, fontSize = 18.sp,
//                modifier = Modifier.align(alignment = Alignment.CenterVertically),
//                fontFamily = Baloo2
//            )
//
//
//    }
//    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
//
//}
//
//@Composable
//fun ProfileStatus(@StringRes label:Int){
//    Row (
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ){
//        Text(
//            text = stringResource(label),
//            color = DarkGreen,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Medium,
//            modifier = Modifier.align(alignment = Alignment.CenterVertically),
//            fontFamily = Baloo2
//        )
//
//            // TODO: depends on database/record
//            AccountStatusBadge(AccountStatus.Active)
//
//
//
//
//    }
//    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewProfile(){
//    ProfileScreen()
//}

package com.example.rasago.theme.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.R
import com.example.rasago.theme.navigation.AppTopBar
import com.example.rasago.ui.theme.Baloo2
import com.example.rasago.ui.theme.DarkGreen
import com.example.rasago.ui.theme.LightGreen

// AccountStatus enum and Badge remain the same...
enum class AccountStatus(val label: String, val color: Color) {
    Active("Active", Color(0xFF4CAF50)),              // Green
    Inactive("Inactive", Color(0xFF9E9E9E)),          // Grey
    Suspended("Suspended", Color(0xFFFF9800)),        // Orange
    Banned("Banned", Color(0xFFF44336)),              // Red
    Pending("Pending Verification", Color(0xFF2196F3)) // Blue
}

@Composable
fun AccountStatusBadge(status: AccountStatus) {
    Box(
        modifier = Modifier
            .background(color = status.color, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = status.label,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBackClick: () -> Unit){
    Scaffold(
        topBar = {
            AppTopBar(title = "Profile", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ){
                Image(
                    painter = painterResource(R.drawable.background_image),
                    contentDescription = stringResource(R.string.background),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit),
                        modifier = Modifier
                            .clickable { /*TODO*/ }
                            .size(32.dp)

                    )
                    Text(
                        text = stringResource(R.string.edit),
                        fontSize = 16.sp,
                        fontFamily = Baloo2,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.default_profile_picture),
                    contentDescription = stringResource(R.string.default_profile_picture),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            }
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(LightGreen)
            ) {
                Column (modifier = Modifier.padding(16.dp)){
                    Text(
                        text = stringResource(id = R.string.personal_information),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        fontFamily = Baloo2
                    )
                }
            }
            ProfileItem(label = R.string.name, value = "Testing")
            ProfileStatus(label = R.string.status)
            ProfileItem(label = R.string.mobile_number, value = "012-2322323")
            ProfileItem(label = R.string.email, value = "testing@gmail.com")
            ProfileItem(label = R.string.dob, value = "25-12-2006")
            ProfileItem(label = R.string.gender, value = "Female")
        }
    }
}

@Composable
fun ProfileItem(@StringRes label:Int , value: String){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = stringResource(label),
            color = DarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            fontFamily = Baloo2
        )
        Text(
            text = value, fontWeight = FontWeight.Medium, fontSize = 18.sp,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            fontFamily = Baloo2
        )
    }
    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}

@Composable
fun ProfileStatus(@StringRes label:Int){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = stringResource(label),
            color = DarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            fontFamily = Baloo2
        )
        AccountStatusBadge(AccountStatus.Active)
    }
    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewProfile(){
    ProfileScreen(onBackClick = {})
}
