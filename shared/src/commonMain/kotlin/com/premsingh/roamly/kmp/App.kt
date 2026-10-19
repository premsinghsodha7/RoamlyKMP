package com.premsingh.roamly.kmp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class RoamTab(val label: String, val glyph: String) {
    Trips("Trips", "⌂"), Plan("Plan", "◷"), Budget("Budget", "€"), Pack("Pack", "✓"),
}

private data class RoamPalette(
    val name: String, val primary: Color, val secondary: Color, val background: Color,
    val surface: Color, val foreground: Color, val muted: Color, val accent: Color, val dark: Boolean,
)

private val palettes = listOf(
    RoamPalette("Coast", Color(0xFF246D6B), Color(0xFFE6F2EF), Color(0xFFF6F8F5), Color.White, Color(0xFF1D3335), Color(0xFF78888A), Color(0xFFF0A56F), false),
    RoamPalette("Dune", Color(0xFF9A593D), Color(0xFFF5E9DD), Color(0xFFFBF7F1), Color.White, Color(0xFF3B302B), Color(0xFF8D7E72), Color(0xFFE8B873), false),
    RoamPalette("Midnight", Color(0xFF8DC7C0), Color(0xFF263B42), Color(0xFF14252C), Color(0xFF20353C), Color(0xFFE6F0ED), Color(0xFFA0B2B2), Color(0xFFF0B57A), true),
)

@Composable
fun App() {
    var selectedTab by remember { mutableStateOf(RoamTab.Trips) }
    var paletteIndex by remember { mutableStateOf(0) }
    var selectedDay by remember { mutableStateOf(1) }
    var extraSpent by remember { mutableStateOf(0) }
    val packed = remember { mutableStateListOf(false, true, false, true, false, false, true) }
    val palette = palettes[paletteIndex]
    val colors = if (palette.dark) {
        darkColorScheme(primary = palette.primary, onPrimary = Color(0xFF10272D), secondary = palette.secondary,
            background = palette.background, surface = palette.surface, onSurface = palette.foreground,
            onBackground = palette.foreground, surfaceVariant = palette.secondary, onSurfaceVariant = palette.muted)
    } else {
        lightColorScheme(primary = palette.primary, onPrimary = Color.White, secondary = palette.secondary,
            background = palette.background, surface = palette.surface, onSurface = palette.foreground,
            onBackground = palette.foreground, surfaceVariant = palette.secondary, onSurfaceVariant = palette.muted)
    }

    MaterialTheme(colorScheme = colors) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).widthIn(max = 900.dp).padding(horizontal = 22.dp)) {
                BrandHeader(palette) { paletteIndex = (paletteIndex + 1) % palettes.size }
                Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
                    Spacer(Modifier.height(16.dp))
                    when (selectedTab) {
                        RoamTab.Trips -> TripsScreen(palette)
                        RoamTab.Plan -> PlanScreen(selectedDay, { selectedDay = it }, palette)
                        RoamTab.Budget -> BudgetScreen(extraSpent, { extraSpent += 18 }, palette)
                        RoamTab.Pack -> PackScreen(packed, { index -> packed[index] = !packed[index] }, palette)
                    }
                    Spacer(Modifier.height(22.dp))
                    Text("A PERSONAL TRIP PLANNER · SAMPLE ITINERARY", Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        color = palette.muted, fontSize = 9.sp, letterSpacing = 1.2.sp, textAlign = TextAlign.Center)
                }
                NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
                    RoamTab.entries.forEach { tab ->
                        NavigationBarItem(selected = selectedTab == tab, onClick = { selectedTab = tab },
                            icon = { Text(tab.glyph, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                            label = { Text(tab.label, fontSize = 11.sp) })
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandHeader(palette: RoamPalette, onThemeClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = palette.primary, shape = RoundedCornerShape(15.dp), modifier = Modifier.size(42.dp)) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("R", color = MaterialTheme.colorScheme.onPrimary, fontSize = 23.sp, fontWeight = FontWeight.Black) }
            }
            Spacer(Modifier.width(11.dp))
            Column {
                Text("roamly", color = palette.foreground, fontSize = 20.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp)
                Text("GO SOMEWHERE GOOD", color = palette.muted, fontSize = 8.sp, letterSpacing = 1.6.sp)
            }
        }
        Surface(color = palette.secondary, shape = RoundedCornerShape(18.dp), modifier = Modifier.clickable(onClick = onThemeClick)) {
            Row(Modifier.padding(horizontal = 13.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("◐", color = palette.primary, fontSize = 15.sp)
                Spacer(Modifier.width(7.dp))
                Text(palette.name, color = palette.foreground, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun TripsScreen(palette: RoamPalette) {
    Text("YOUR NEXT ESCAPE", color = palette.muted, fontSize = 10.sp, letterSpacing = 1.6.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(10.dp))
    LisbonFeatureCard()
    Spacer(Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MetricCard("5 days", "IN THE CITY", Modifier.weight(1f), palette)
        MetricCard("€1,240", "TRIP BUDGET", Modifier.weight(1f), palette)
        MetricCard("7 saved", "PLACES", Modifier.weight(1f), palette)
    }
    Spacer(Modifier.height(23.dp))
    SectionHeading("A good day, planned", "DAY 2 · ALFAMA", palette)
    Spacer(Modifier.height(10.dp))
    TimelineStop("09:15", "Coffee at Hello, Kristof", "A slow start and a flat white", "☕", palette)
    TimelineStop("11:00", "Ride Tram 28", "Board at Martim Moniz · 25 min", "↗", palette)
    TimelineStop("13:30", "Lunch in Alfama", "Table for two · saved place", "✦", palette)
    Spacer(Modifier.height(10.dp))
    Text("Your trip, your pace. Move plans around whenever you like.", color = palette.muted, fontSize = 11.sp)
    Spacer(Modifier.height(18.dp))
    SectionHeading("Trip notes", "LISBON · PORTUGAL", palette)
    InfoCard("Stay", "Casa do Mercado", "Check-in Nov 4 · Baixa district", "⌂", palette)
}

@Composable
private fun LisbonFeatureCard() {
    Box(Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(28.dp))
        .background(Brush.linearGradient(listOf(Color(0xFF183D49), Color(0xFF427B78), Color(0xFFD89464))))) {
        Box(Modifier.align(Alignment.TopEnd).padding(top = 25.dp, end = 31.dp).size(68.dp).background(Color(0xFFFFD79A), CircleShape))
        Box(Modifier.align(Alignment.BottomEnd).padding(end = 8.dp).size(142.dp).background(Color(0xFFE5B778), RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp)))
        Box(Modifier.align(Alignment.BottomEnd).padding(end = 53.dp).size(112.dp).background(Color(0xFFE77F64), RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp)))
        Box(Modifier.align(Alignment.BottomEnd).padding(end = 81.dp).width(18.dp).height(68.dp).background(Color(0xFFF3E2C6), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)))
        Column(Modifier.align(Alignment.TopStart).padding(20.dp)) {
            Surface(color = Color(0x33FFFFFF), shape = CircleShape) {
                Text("UP NEXT  ·  16 DAYS", Modifier.padding(horizontal = 11.dp, vertical = 7.dp), color = Color.White, fontSize = 9.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(17.dp))
            Text("Lisbon", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 34.sp, letterSpacing = (-1).sp)
            Text("Portugal  ·  Nov 4 — 8", color = Color(0xFFEAF2EA), fontSize = 12.sp)
            Spacer(Modifier.height(20.dp))
            Text("A little more wandering.\nA little less rushing.", color = Color.White, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium)
        }
        Text("✦  ALFAMA", Modifier.align(Alignment.BottomEnd).padding(end = 17.dp, bottom = 12.dp), color = Color.White, fontSize = 9.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MetricCard(value: String, caption: String, modifier: Modifier, palette: RoamPalette) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(18.dp), modifier = modifier) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 15.dp)) {
            Text(value, color = palette.foreground, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Spacer(Modifier.height(5.dp))
            Text(caption, color = palette.muted, fontSize = 7.sp, letterSpacing = 0.9.sp, maxLines = 1)
        }
    }
}

@Composable
private fun SectionHeading(title: String, eyebrow: String, palette: RoamPalette) {
    Column {
        Text(eyebrow, color = palette.primary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.3.sp)
        Spacer(Modifier.height(4.dp))
        Text(title, color = palette.foreground, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TimelineStop(time: String, title: String, subtitle: String, glyph: String, palette: RoamPalette) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.width(48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(time, color = palette.muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(5.dp))
            Box(Modifier.size(7.dp).background(palette.primary, CircleShape))
        }
        Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(18.dp), modifier = Modifier.weight(1f)) {
            Row(Modifier.padding(horizontal = 13.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(color = palette.secondary, shape = RoundedCornerShape(12.dp), modifier = Modifier.size(36.dp)) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(glyph, color = palette.primary, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
                }
                Spacer(Modifier.width(11.dp))
                Column {
                    Text(title, color = palette.foreground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(subtitle, color = palette.muted, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun PlanScreen(selectedDay: Int, onDaySelected: (Int) -> Unit, palette: RoamPalette) {
    Text("Lisbon, day by day", color = palette.foreground, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Text("A flexible outline for your five days.", color = palette.muted, fontSize = 12.sp)
    Spacer(Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        (1..5).forEach { day ->
            val selected = selectedDay == day
            Surface(color = if (selected) palette.primary else MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp), modifier = Modifier.clickable { onDaySelected(day) }) {
                Column(Modifier.padding(horizontal = 17.dp, vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("DAY " + day, color = if (selected) Color.White else palette.muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("NOV " + (3 + day), color = if (selected) Color.White else palette.foreground, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
    Spacer(Modifier.height(19.dp))
    val heading = when (selectedDay) {
        1 -> "Arrival & Baixa"; 2 -> "Alfama & the river"; 3 -> "Belém by the water"
        4 -> "A day in Sintra"; else -> "One last pastel de nata"
    }
    SectionHeading(heading, "DAY PLAN · NOVEMBER " + (3 + selectedDay), palette)
    Spacer(Modifier.height(10.dp))
    val stops = when (selectedDay) {
        1 -> listOf("15:20" to ("Land at Humberto Delgado" to "Metro to Baixa-Chiado"), "17:00" to ("Check in at Casa do Mercado" to "Drop your bags and take a breath"), "19:30" to ("Dinner at Taberna da Rua" to "Saved · walking distance"))
        2 -> listOf("09:15" to ("Coffee at Hello, Kristof" to "A slow start and a flat white"), "11:00" to ("Ride Tram 28" to "Board at Martim Moniz · 25 min"), "13:30" to ("Lunch in Alfama" to "Table for two · saved place"), "16:00" to ("Lookout at Portas do Sol" to "Golden hour over the rooftops"))
        3 -> listOf("09:30" to ("Train to Belém" to "Depart Cais do Sodré"), "11:00" to ("Jerónimos Monastery" to "Tickets saved to your trip"), "13:00" to ("Pastéis de Belém" to "Go early for a shorter line"))
        4 -> listOf("08:40" to ("Train from Rossio" to "Use the Viva Viagem card"), "10:00" to ("Explore Quinta da Regaleira" to "Timed entry · 10:00"), "15:30" to ("Return to Lisbon" to "Keep the evening open"))
        else -> listOf("09:30" to ("Breakfast near the hotel" to "Pick a place when you wake up"), "12:00" to ("Browse Feira da Ladra" to "Check opening day first"), "17:00" to ("Head to the airport" to "Allow time for the metro"))
    }
    stops.forEach { (time, detail) -> TimelineStop(time, detail.first, detail.second, "✦", palette) }
    Spacer(Modifier.height(16.dp))
    InfoCard("A little flexibility", "Keep the afternoon open", "Plans are suggestions. Tap a day above to explore.", "↗", palette)
}

@Composable
private fun BudgetScreen(extraSpent: Int, onAddExpense: () -> Unit, palette: RoamPalette) {
    Text("Trip budget", color = palette.foreground, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Text("A calm view of the money set aside.", color = palette.muted, fontSize = 12.sp)
    Spacer(Modifier.height(15.dp))
    Surface(color = palette.primary, shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp)) {
            Text("SPENT SO FAR", color = Color(0xFFD9ECE6), fontSize = 9.sp, letterSpacing = 1.3.sp)
            Spacer(Modifier.height(8.dp))
            Text("€" + (386 + extraSpent), color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            Text("of €1,240 trip budget", color = Color(0xFFD9ECE6), fontSize = 11.sp)
            Spacer(Modifier.height(14.dp))
            Box(Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(Color(0x553D7F7B))) {
                Box(Modifier.fillMaxWidth(((386 + extraSpent).toFloat() / 1240f).coerceIn(0f, 1f)).height(8.dp).background(palette.accent, CircleShape))
            }
            Spacer(Modifier.height(7.dp))
            Text("€" + (854 - extraSpent) + " left to enjoy", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
    Spacer(Modifier.height(16.dp))
    SectionHeading("Where it’s going", "PLANNED & TRACKED", palette)
    Spacer(Modifier.height(10.dp))
    BudgetCategory("Stay", "3 nights · paid", 420, 1240, palette.primary, palette)
    BudgetCategory("Food & coffee", "12 places saved", 168, 1240, Color(0xFFE59B65), palette)
    BudgetCategory("Getting around", "Metro + tram", 54, 1240, Color(0xFF719BC0), palette)
    BudgetCategory("Experiences", "Tickets & entry", 62 + extraSpent, 1240, Color(0xFF9883B9), palette)
    Spacer(Modifier.height(12.dp))
    Button(onClick = onAddExpense, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(containerColor = palette.primary)) {
        Text("＋  Add sample expense · €18", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.SemiBold)
    }
    Spacer(Modifier.height(8.dp))
    Text("Demo only. Expenses stay on this screen and reset when the app restarts.", color = palette.muted, fontSize = 10.sp)
}

@Composable
private fun BudgetCategory(name: String, detail: String, amount: Int, total: Int, color: Color, palette: RoamPalette) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text(name, color = palette.foreground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold); Text(detail, color = palette.muted, fontSize = 10.sp) }
            Text("€" + amount, color = palette.foreground, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(7.dp))
        Box(Modifier.fillMaxWidth().height(5.dp).clip(CircleShape).background(palette.secondary)) {
            Box(Modifier.fillMaxWidth((amount.toFloat() / total).coerceIn(0f, 1f)).height(5.dp).background(color, CircleShape))
        }
    }
}

@Composable
private fun PackScreen(packed: List<Boolean>, onToggle: (Int) -> Unit, palette: RoamPalette) {
    val count = packed.count { it }
    Text("Pack light, go far", color = palette.foreground, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Text("The useful things, without the overthinking.", color = palette.muted, fontSize = 12.sp)
    Spacer(Modifier.height(15.dp))
    Surface(color = palette.secondary, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(17.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Your Lisbon list", color = palette.foreground, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(count.toString() + " of " + packed.size + " packed", color = palette.muted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Box(Modifier.size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center) {
                Text((count * 100 / packed.size).toString() + "%", color = palette.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
    Spacer(Modifier.height(17.dp))
    SectionHeading("Travel essentials", "DON’T FORGET THESE", palette)
    Spacer(Modifier.height(7.dp))
    val items = listOf(
        "Passport & travel documents" to "Keep them somewhere easy to reach",
        "Comfortable walking shoes" to "Those Lisbon hills are real",
        "Reusable water bottle" to "Refill before a long wander",
        "Power adapter" to "Portugal uses Type C and F",
        "Sunglasses" to "For long lunches in the sun",
        "Light layer for evenings" to "The breeze picks up by the river",
        "Camera or charger" to "Save a little room for memories",
    )
    items.forEachIndexed { index, item ->
        val checked = packed[index]
        Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(17.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onToggle(index) }) {
            Row(Modifier.padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(color = if (checked) palette.primary else palette.secondary, shape = RoundedCornerShape(8.dp), modifier = Modifier.size(23.dp)) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { if (checked) Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(item.first, color = palette.foreground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(item.second, color = palette.muted, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun InfoCard(eyebrow: String, title: String, detail: String, glyph: String, palette: RoamPalette) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = palette.secondary, shape = RoundedCornerShape(13.dp), modifier = Modifier.size(42.dp)) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(glyph, color = palette.primary, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(eyebrow.uppercase(), color = palette.primary, fontSize = 8.sp, letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold)
                Text(title, color = palette.foreground, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(detail, color = palette.muted, fontSize = 10.sp)
            }
        }
    }
}
