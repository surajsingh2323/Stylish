package com.example.stylish.presentation.products

import android.R
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.stylish.data.dto.Product
import kotlinx.coroutines.launch
import com.example.stylish.domain.util.Result
import com.example.stylish.presentation.Wishlist.WishlistViewModel
import com.example.stylish.presentation.cart.CartViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavHostController,
    productId: Int,
    productViewModel: ProductViewModel = hiltViewModel(),
    wishlistViewModel: WishlistViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()

){
    val productsState by productViewModel.productsState.collectAsState()
    var selectedImageIndex by remember{ mutableStateOf(0) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isFavorite by remember{ mutableStateOf(false) }

    // Check if product is in wishlist
    LaunchedEffect(productId) {
        isFavorite = wishlistViewModel.isInWishlist(productId)
    }

    // Share function
    fun shareProduct(product: Product){
        val shareText = "Check out this amazing product: ${product.title}\n\n" +
                "${product.description}\n\n" +
                "Price: ₹${String.format("%.0f",product.price * 83)}\n" +
                "Rating: ${String.format("%.1f",product.rating)} stars\n\n" +
                "Get it now on EcoMart!"

        val shareIntent = Intent().apply{
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Check out ${product.title}")
        }

        val chooserIntent = Intent.createChooser(shareIntent,"Share Product")
        context.startActivity(chooserIntent)
    }

    // Find the product by ID
    val product = when (val state = productsState){
        is Result.Success -> state.data.find { it.id == productId }
        else -> null
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Product Details")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        product?.let { shareProduct(it)}
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = {
                        product?.let { prod ->
                            coroutineScope.launch {
                                if (isFavorite){
                                    wishlistViewModel.removeFromWishlist(prod.id)
                                    isFavorite = false
                                } else {
                                    wishlistViewModel.addToWishlist(prod)
                                    isFavorite = true
                                }
                            }
                        }
                    }){
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ){ paddingValues ->
        when {
            productsState is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            product == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text("Product not found")
                }
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ){
                    // Product Images
                    ProductImageGallery(
                        product = product,
                        selectedImageIndex = selectedImageIndex,
                        onImageSelected = { selectedImageIndex = it }
                    )

                    // Product Details
                    ProductDetailsSection(product = product)

                    // Add to Cart Button
                    AddToCartSection(
                        product = product,
                        onAddToCart = {
                            product?.let{ prod ->
                                cartViewModel.addToCart(prod,1)
                                Toast.makeText(
                                    context,
                                    "${prod.title} added to cart",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductImageGallery(
    product: Product,
    selectedImageIndex: Int,
    onImageSelected: (Int) -> Unit
){
    val context = LocalContext.current
    val images = if (product.images.isNotEmpty()) product.images else listOf(product.thumbnail)

    Column{
        // Main Image
        Card (
            modifier = Modifier.fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ){
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(images.getOrNull(selectedImageIndex) ?: product.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
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
                            text = "Image not available",
                            color = Color.Gray
                        )
                    }
                }
            )
        }

        // Image Thumbnails
        if (images.size > 1){
            LazyRow (
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(images.size){ index ->
                    Card (
                        modifier = Modifier.size(60.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (index == selectedImageIndex)
                            Color(0xFF4285F4).copy(0.1f) else Color.White
                        ),
                        onClick = { onImageSelected(index) }
                    ){
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(images[index])
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ){
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductDetailsSection(product: Product){
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        // Brand
        if (product.brand.isNotEmpty()){
            Text(
                text = product.brand,
                fontSize = 14.sp,
                color = Color(0xFF4285F4),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Title
        Text(
            text = product.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Rating and Reviews
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            StarRating(rating = product.rating, starSize = 20.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${String.format("%.1f", product.rating)} (${(product.rating * 100).toInt()} reviews) ",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price Section
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "₹${String.format("%.0f", product.price * 83)}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            if (product.discountPercentage > 0){
                Spacer(modifier = Modifier.width(12.dp))
                val originalPrice = product.price / (1 - product.discountPercentage / 100)
                Text(
                    text = "₹${String.format("%.0f", originalPrice * 83)}",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${String.format("%.0f", product.discountPercentage)}% off",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Category
        if (product.category.isNotEmpty()){
            Text(
                text = "Category: ${product.category.replaceFirstChar { it.uppercase() }}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Stock Status
        Text(
            text = if(product.stock > 0) "In Stock (${product.stock} available)" else "Out of Stock",
            fontSize = 14.sp,
            color = if (product.stock > 0) Color(0xFF4CAF50) else Color.Red,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = "Description",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.description,
            fontSize = 16.sp,
            color = Color.Gray,
            lineHeight = 24.sp
        )
    }
}
@Composable
private fun AddToCartSection(
    product: Product,
    onAddToCart: () -> Unit
){
    Column (
        modifier = Modifier
            .padding(16.dp)
            .navigationBarsPadding() // ✅ ADDED: Prevents buttons from hiding under system nav bar
    ){
        // ✅ CHANGED: Wrapped buttons in a Row to save vertical space
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Gap between buttons
        ) {
            // Add to Cart Button
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .weight(1f) // ✅ CHANGED: Uses weight so buttons share width equally
                    .height(50.dp), // Slightly reduced height to ensure visibility
                enabled = product.stock > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4285F4),
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (product.stock > 0) "Add to Cart" else "Out of Stock",
                    fontSize = 14.sp, // Slightly smaller font to fit in Row
                    fontWeight = FontWeight.Bold
                )
            }

            // Buy Now Button
            Button(
                onClick = { /* Handle buy now */},
                modifier = Modifier
                    .weight(1f) // ✅ CHANGED: Uses weight so buttons share width equally
                    .height(50.dp),
                enabled = product.stock > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B35),
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (product.stock > 0) "Buy Now" else "Out of Stock",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}