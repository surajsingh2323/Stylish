package com.example.stylish.presentation.products

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.stylish.Navigation.Routes
import com.example.stylish.R
import com.example.stylish.data.dto.Product
import com.example.stylish.domain.util.Result
import com.example.stylish.presentation.components.BottomNavItem
import com.example.stylish.presentation.components.BottomNavigationBar
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductListScreen(
    navController: NavHostController,
    productViewModel: ProductViewModel = hiltViewModel()
){
    val productsState by productViewModel.productsState.collectAsState()
    val profilePhotoUrl by productViewModel.profilePhotoUrl.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showCategoryFilter by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.brand_name_icon),
                            contentDescription = "Brand Logo",
                            modifier = Modifier.height(32.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "Menu",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.SettingsScreen) }) {
                        if (profilePhotoUrl != null){
                            AsyncImage(
                                model = profilePhotoUrl,
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color(0xFFE0E0E0), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_profile_avatar),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color(0xFFE0E0E0), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "home",
                onItemClick = { item ->
                    when(item){
                        is BottomNavItem.Home -> {
                            // Already on home/product list
                        }
                        is BottomNavItem.Wishlist -> {
                            navController.navigate(Routes.WishlistScreen)
                        }
                        is BottomNavItem.Cart -> {
                            navController.navigate(Routes.CartScreen)
                        }
                        is BottomNavItem.Search -> {
                            navController.navigate(Routes.SearchScreen)
                        }
                        is BottomNavItem.Settings -> {
                            navController.navigate(Routes.SettingsScreen)
                        }
                    }
                }
            )
        }
    ){ paddingValues ->

        // ✅ FIX: Replaced Column + LazyVerticalGrid with a single LazyColumn
        // Root cause: LazyVerticalGrid inside a Column has no bounded height → products can't scroll
        // Fix: One LazyColumn owns all scrolling. Products rendered as chunked(2) Rows
        // which gives the exact same 2-column grid look with no nesting conflict

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFf9f9f9)),
            contentPadding = PaddingValues(bottom = 80.dp) // prevents bottom nav overlap
        ) {

            // ── Search Bar ──────────────────────────────────────────────────
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { navController.navigate(Routes.SearchScreen) },
                    placeholder = {
                        Text(
                            text = "Search any Product..",
                            color = Color(0xFFBBBBBB),
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_magnifier),
                            contentDescription = "Search",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { navController.navigate(Routes.SearchScreen) }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_mic),
                                contentDescription = "Voice Search",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    enabled = false,
                    readOnly = true
                )
            }

            // ── Banner Pager ────────────────────────────────────────────────
            item {
                BannerPager()
            }

            // ── Items count and filter row ──────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    when(val state = productsState){
                        is Result.Success -> {
                            Text(
                                text = "${state.data.size} Items",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        else -> {
                            Text(
                                text = "Loading...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Row {
                        TextButton(onClick = { /* Handle sort */ }) {
                            Text("Sort")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Sort",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        TextButton(onClick = { showCategoryFilter = !showCategoryFilter }) {
                            Text(
                                text = "Filter",
                                color = if (showCategoryFilter) MaterialTheme.colorScheme.primary else Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Filter",
                                modifier = Modifier.size(16.dp),
                                tint = if (showCategoryFilter) MaterialTheme.colorScheme.primary else Color.Unspecified
                            )
                        }
                    }
                }
            }

            // ── Category Section (visible only when filter is clicked) ──────
            if (showCategoryFilter){
                item {
                    CategorySection(viewModel = productViewModel)
                }
            }

            // ── Product Grid ────────────────────────────────────────────────
            when (val state = productsState) {

                is Result.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator()
                        }
                    }
                }

                is Result.Success -> {
                    // chunked(2) → splits product list into pairs
                    // each pair rendered as a Row with 2 cards side by side
                    // weight(1f) on each card → both cards share equal width automatically
                    items(state.data.chunked(2)) { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { product ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ProductCard(
                                        product = product,
                                        onClick = {
                                            navController.navigate(
                                                Routes.ProductDetailScreen(product.id)
                                            )
                                        }
                                    )
                                }
                            }
                            // If last row has only 1 product, fill the empty right slot
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                is Result.Failure -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error: ${state.message}",
                                    color = Color.Red
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                TextButton(onClick = { productViewModel.retryLoading() }) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                }

                Result.Idle -> {
                    // Initial state - nothing to show
                }
            }
        }
    }
}

// ── ProductCard ─────────────────────────────────────────────────────────────
// No changes made to this composable

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
){
    val context = LocalContext.current

    fun shareProduct(product: Product){
        val shareText = "Check out this amazing product: ${product.title}\n\n" +
                "${product.description}\n\n" +
                "Price: ₹${String.format("%.0f", product.price * 83)}\n" +
                "Rating: ${String.format("%.1f", product.rating)} stars\n\n" +
                "Get it now on EcoMart!"

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Check out ${product.title}")
        }

        val chooserIntent = Intent.createChooser(shareIntent, "Share Product")
        context.startActivity(chooserIntent)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){
        Column(
            modifier = Modifier.padding(12.dp)
        ){
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
                            text = "No Image",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.description,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column {
                    Text(
                        text = "₹${String.format("%.0f", product.price * 83)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (product.discountPercentage > 0){
                        val originalPrice = product.price / (1 - product.discountPercentage / 100)
                        Text(
                            text = "₹${String.format("%.0f", originalPrice * 83)}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically){
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
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { shareProduct(product) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── StarRating ───────────────────────────────────────────────────────────────
// No changes made to this composable

@Composable
fun StarRating(
    rating: Double,
    starSize: Dp = 16.dp,
    maxStars: Int = 5
){
    Row {
        repeat(maxStars) { index ->
            val starRating = when {
                rating >= index + 1 -> 1.0
                rating > index -> rating - index
                else -> 0.0
            }

            Box {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFE0E0E0),
                    modifier = Modifier.size(starSize)
                )
                if (starRating > 0){
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFA726),
                        modifier = Modifier
                            .size(starSize)
                            .clipToBounds()
                            .drawWithContent {
                                clipRect(right = size.width * starRating.toFloat()) {
                                    this@drawWithContent.drawContent()
                                }
                            }
                    )
                }
            }
        }
    }
}

// ── Category data class, CategorySection, CategoryItem ──────────────────────
// No changes made to these

data class Category(
    val name: String,
    val imageRes: Int,
    val slug: String
)

@Composable
fun CategorySection(viewModel: ProductViewModel){
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val categoryImageMap = mapOf(
        "beauty" to R.drawable.round_beauty_image,
        "fragrances" to R.drawable.round_fashion_image,
        "furniture" to R.drawable.round_kids_image,
        "mens-shirts" to R.drawable.round_mens_image,
        "mens-shoes" to R.drawable.round_mens_image,
        "mens-watches" to R.drawable.round_mens_image,
        "womens-bags" to R.drawable.round_womens_image,
        "womens-dresses" to R.drawable.round_womens_image,
        "womens-jewellery" to R.drawable.round_womens_image,
        "womens-shoes" to R.drawable.round_womens_image,
        "womens-watches" to R.drawable.round_womens_image
    )

    val categories = listOf(
        Category("Beauty", R.drawable.round_beauty_image, "beauty"),
        Category("Fashion", R.drawable.round_fashion_image, "fragrances"),
        Category("Kids", R.drawable.round_kids_image, "furniture"),
        Category("Mens", R.drawable.round_mens_image, "mens-shirts"),
        Category("Womens", R.drawable.round_womens_image, "womens-dresses")
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(categories){ category ->
            CategoryItem(
                category = category,
                isSelected = selectedCategory == category.slug,
                onClick = {
                    if (selectedCategory == category.slug){
                        viewModel.clearCategoryFilter()
                    } else {
                        viewModel.filterByCategory(category.slug)
                    }
                }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .clickable { onClick() }
    ){
        Image(
            painter = painterResource(id = category.imageRes),
            contentDescription = category.name,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFE0E0E0),
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ── BannerPager ──────────────────────────────────────────────────────────────
// No changes made to this composable

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun BannerPager(){
    val banners = listOf(
        R.drawable.product_banner_1,
        R.drawable.product_banner_2,
        R.drawable.product_banner_3,
    )

    val pagerState = rememberPagerState(pageCount = { banners.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true){
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ){
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        ) { page ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = banners[page]),
                    contentDescription = "Banner ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ){
            repeat(banners.size){ index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) Color.White
                            else Color.White.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}