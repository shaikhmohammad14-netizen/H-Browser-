package com.hbrowser

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF071426)
private val Blue = Color(0xFF3B82F6)
private val Glass = Color(0xCC14243A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HBrowserApp() }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HBrowserApp() {
    var address by remember { mutableStateOf("https://www.google.com") }
    var pageTitle by remember { mutableStateOf("H Browser") }
    var progress by remember { mutableIntStateOf(0) }
    var canBack by remember { mutableStateOf(false) }
    var canForward by remember { mutableStateOf(false) }
    var reloadKey by remember { mutableIntStateOf(0) }
    var homeKey by remember { mutableIntStateOf(0) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var pressed by remember { mutableStateOf(false) }

    BackHandler(enabled = canBack) { webView?.goBack() }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Color(0xFF8AB4FF),
        background = Navy,
        surface = Glass,
        onSurface = Color.White
    )) {
        Column(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color(0xFF102A48), Navy, Color(0xFF030812)))
            )
        ) {
            Column(Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("H", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text("H BROWSER", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        Text(pageTitle, color = Color(0xFFB9C8DC), fontSize = 11.sp, maxLines = 1)
                    }
                    Surface(color = Color(0x332E90FF), shape = RoundedCornerShape(50)) {
                        Text("PRIVATE", Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            color = Color(0xFFB9D8FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    placeholder = { Text("Search or enter website") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(onGo = { webView?.loadUrl(normalizeUrl(address)) }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        focusedBorderColor = Blue, unfocusedBorderColor = Color(0x557FA7D8),
                        focusedContainerColor = Glass, unfocusedContainerColor = Glass,
                        cursorColor = Color.White
                    )
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    NavButton(Icons.Default.ArrowBack, "Back", !canBack) { webView?.goBack() }
                    NavButton(Icons.Default.ArrowForward, "Forward", !canForward) { webView?.goForward() }
                    NavButton(Icons.Default.Refresh, "Reload", false) { webView?.reload() }
                    NavButton(Icons.Default.Home, "Home", false) { webView?.loadUrl("https://www.google.com") }
                    Spacer(Modifier.weight(1f))
                    Text("WEB", color = Color(0xFFB9C8DC), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                if (progress in 1..99) {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(progress = { progress / 100f }, modifier = Modifier.fillMaxWidth().height(3.dp))
                }
            }

            AndroidView(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 5.dp),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadsImagesAutomatically = true
                        settings.allowFileAccess = false
                        settings.allowContentAccess = false
                        settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean = false
                            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                address = url ?: address
                                canBack = view?.canGoBack() == true
                                canForward = view?.canGoForward() == true
                            }
                            override fun onPageFinished(view: WebView?, url: String?) {
                                address = url ?: address
                                pageTitle = view?.title ?: "H Browser"
                                canBack = view?.canGoBack() == true
                                canForward = view?.canGoForward() == true
                            }
                        }
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) { progress = newProgress }
                            override fun onReceivedTitle(view: WebView?, title: String?) { pageTitle = title ?: "H Browser" }
                        }
                        webView = this
                        loadUrl("https://www.google.com")
                    }
                },
                update = { view ->
                    webView = view
                    if (reloadKey > 0) { view.reload(); reloadKey = 0 }
                    if (homeKey > 0) { view.loadUrl("https://www.google.com"); homeKey = 0 }
                }
            )
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(22.dp)).background(Glass).padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("H Browser", color = Color.White, fontWeight = FontWeight.SemiBold)
                Text("Chromium WebView", color = Color(0xFFB9C8DC), fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun NavButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, disabled: Boolean, onClick: () -> Unit) {
    var down by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (down) 0.92f else 1f, label = "buttonScale")
    FilledTonalIconButton(
        onClick = onClick,
        enabled = !disabled,
        modifier = Modifier.size(42.dp).scale(scale),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = Glass, contentColor = Color.White,
            disabledContainerColor = Color(0x3314243A),
            disabledContentColor = Color(0x667F91A8)
        )
    ) { Icon(icon, contentDescription = label) }
}

private fun normalizeUrl(input: String): String {
    val value = input.trim()
    if (value.isEmpty()) return "https://www.google.com"
    if (value.contains(" ") || !value.contains(".")) {
        return "https://www.google.com/search?q=" + android.net.Uri.encode(value)
    }
    return if (value.startsWith("http://") || value.startsWith("https://")) value else "https://$value"
}
