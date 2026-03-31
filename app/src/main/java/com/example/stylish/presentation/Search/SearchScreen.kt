package com.example.stylish.presentation.Search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.stylish.Navigation.Routes
import com.example.stylish.R
import com.example.stylish.data.dto.Product
import com.example.stylish.presentation.products.ProductViewModel
import com.example.stylish.domain.util.Result
import com.example.stylish.presentation.products.StarRating

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    productViewModel: ProductViewModel = hiltViewModel()
){
    val productsState by productViewModel.productsState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val recentSearches = remember { mutableStateListOf<String>() }
    val focusRequester = remember { FocusRequester() }

    // Popular search suggestions
    val popularSearches = listOf(
        "Smartphone",
        "Laptop",
        "Headphones",
        "Watch",
        "Shoes",
        "Dress",
        "Furniture",
        "Beauty Products"
    )

    // Request focus on search field when screen opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            if (it.isNotEmpty()){
                                productViewModel.searchProducts(it)
                            }else{
                                productViewModel.loadProducts()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        placeholder = {
                            Text(
                                text = "Search products...",
                                color = Color(0xFFBBBBBB),
                                fontSize = 16.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF666666)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()){
                                IconButton(
                                    onClick = {
                                        searchQuery = ""
                                        productViewModel.loadProducts()
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Color(0xFF666666)
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = androidx.compose.material3.TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            disabledContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF9F9F9))
        ){
            // Show suggestions when search is empty
            if (searchQuery.isEmpty()){
                LazyColumn (
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ){
                    // Recent Searches Section
                    if(recentSearches.isNotEmpty()){
                        item{
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "Recent Searches",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                TextButton(
                                    onClick = { recentSearches.clear() }
                                ) {
                                    Text(
                                        text = "Clear All",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        items(recentSearches) { search ->
                            RecentSearchItem(
                                searchText = search,
                                onClick = {
                                    searchQuery = search
                                    productViewModel.searchProducts(search)
                                },
                                onDelete = {
                                    recentSearches.remove(search)
                                }
                            )
                        }

                            item{
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }

                        // Popular Searches Section
                        item{
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Icon(
                                    painterResource(R.drawable.trending_up),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Popular Searches",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        item {
                            LazyRow (
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                items(popularSearches){ search ->
                                    SuggestionChip(
                                        onClick = {
                                            searchQuery = search
                                            if (!recentSearches.contains(search)){
                                                recentSearches.add(0, search)
                                                if (recentSearches.size > 10){
                                                    recentSearches.removeAt(recentSearches.size - 1)
                                                }
                                            }
                                            productViewModel.searchProducts(search)
                                        },
                                        label = { Text(search)},
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        item{
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        //  Search Tips
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE3F2FD)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ){
                                Column (
                                    modifier = Modifier.padding(16.dp)
                                ){
                                    Text(
                                        text = "Search Tips",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1976D2)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "• Search by product name, brand, or category\n" +
                                                "• Use specific keywords for better results\n" +
                                                "• Try popular searches for trending products",
                                        fontSize = 14.sp,
                                        color = Color(0xFF424242),
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Show search results
                    when (val state = productsState){
                        is Result.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                CircularProgressIndicator()
                            }
                        }
                        is Result.Success -> {
                            if (state.data.isEmpty()) {
                                // No results found
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(32.dp)
                                    ){
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = null,
                                            modifier = Modifier.size(80.dp),
                                            tint = Color(0xFFBBBBBB)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "No results found",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Try searching with different keywords",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            } else {
                                Column {
                                    // Results count
                                    Text(
                                        text = "${state.data.size} results found",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )

                                    // Product Grid
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(state.data){ product ->
                                            SearchProductCard(
                                                product = product,
                                                onClick  = {
                                                    // Add to recent searches
                                                    if (!recentSearches.contains(searchQuery)){
                                                        recentSearches.add(0,searchQuery)
                                                        if (recentSearches.size > 10){
                                                            recentSearches.removeAt(recentSearches.size - 1)
                                                        }
                                                    }
                                                    navController.navigate(Routes.ProductDetailScreen(product.id))
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        is Result.Failure -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Text(
                                        text = "Error: ${state.message}",
                                        color = Color.Red
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextButton(
                                        onClick = { productViewModel.searchProducts(searchQuery)}
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                        Result.Idle -> {
                            // Initial state
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun RecentSearchItem(
        searchText: String,
        onClick: () -> Unit,
        onDelete: () -> Unit
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable{ onClick()}
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ){
                Icon(
                    painterResource(R.drawable.history),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = searchText,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    @Composable
    fun SearchProductCard(
        product: Product,
        onClick: () -> Unit
    ){
        val context = LocalContext.current

        Card (
            modifier = Modifier
                .fillMaxWidth()
                .clickable{ onClick()},
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ){
            Column(
                modifier = Modifier.padding(12.dp)
            ){
                // Product Image
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(product.thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5)),
                    contentScale = ContentScale.Fit,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text="No Image",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Product Title
                Text(
                    text = product.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Product Brand
                Text(
                    text = product.brand,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price and Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Column {
                        // Discounted Price
                        Text(
                            text = "₹${String.format("%.0f", product.price * 83)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        // Original Price (if there's a discount)
                        if(product.discountPercentage > 0){
                            val originalPrice = product.price / (1 - product.discountPercentage / 100)
                            Text(
                                text = "₹${String.format("%.0f", originalPrice * 83)}",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        StarRating(
                            rating = product.rating,
                            starSize = 14.dp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", product.rating),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
      }