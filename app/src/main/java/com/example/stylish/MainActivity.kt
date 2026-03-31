package com.example.stylish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.stylish.Navigation.AppNavigation
import com.example.stylish.ui.theme.StylishTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                      AppNavigation() // Parameter has to be passed
        }
    }
}

/*****************************LAZY COLUMN***********************************/
/*  @Composable
  fun ContactListApp(){
    val contacts = listOf(
        Contact("Suraj","8799956764"),
        Contact("Deepak","7678495885"),
        Contact("Saurabh","3457755977"),
        Contact("Ramesh","6558987475"),
        Contact("Suresh","7878578948"),
        Contact("Amit","9875885757"),

        Contact("Deepak","7678495885"),
        Contact("Saurabh","3457755977"),
        Contact("Ramesh","6558987475"),
        Contact("Suresh","7878578948"),
        Contact("Amit","9875885757"),
        Contact("Suraj","8799956764"),
        Contact("Deepak","7678495885"),
        Contact("Saurabh","3457755977"),
        Contact("Ramesh","6558987475"),
        Contact("Suresh","7878578948"),
        Contact("Amit","9875885757"),
        Contact("Suraj","8799956764"),
        Contact("Deepak","7678495885"),
        Contact("Saurabh","3457755977"),
        Contact("Ramesh","6558987475"),
        Contact("Suresh","7878578948"),
        Contact("Amit","9875885757"),
        Contact("Amit","9875885757"),
        Contact("Amit","9875885757"),
        Contact("Amit","9875885757"),
        Contact("Amit","9875885757"),
        Contact("Amit","9875885757"),
        Contact("Amit","9875885757"),
        Contact("Amit","9875885757"),
        Contact("Amit","9875885757")
    )
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        items(contacts){ contact -> // starts from '0'
            ContactItem(contact = contact )
        }
    }
}

@Composable
fun ContactItem(contact: Contact){ // became dynamic
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD0D01E))
    ) {
        Column(modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = contact.phone, // if type int used we have to convert it by .toString as this is a text
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPrev(){
    StylishTheme {
        ContactListApp()
    }
}
*/