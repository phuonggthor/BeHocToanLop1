package com.example.behoctoanlop1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ManHinhChinh()
            }
        }
    }
}

class CauHoi(val a: Int, val b: Int, val dau: String, val dapAn: Int)

fun taoCauHoi(kieu: Int, max: Int): CauHoi {
    var phep = kieu
    if (kieu == 2) {
        phep = Random.nextInt(2)
    }

    if (phep == 0) {
        val a = Random.nextInt(0, max + 1)
        val b = Random.nextInt(0, max - a + 1) // de a + b khong vuot qua max
        return CauHoi(a, b, "+", a + b)
    } else {
        val a = Random.nextInt(0, max + 1)
        val b = Random.nextInt(0, a + 1) // de a - b khong am
        return CauHoi(a, b, "-", a - b)
    }
}

fun taoDapAn(dung: Int, max: Int): List<Int> {
    val ds = mutableListOf(dung)
    while (ds.size < 4) {
        val x = Random.nextInt(0, max + 1)
        if (x !in ds) {
            ds.add(x)
        }
    }
    return ds.shuffled()
}

const val TONG_SO_CAU = 10

@Composable
fun ManHinhChinh() {
    var man by remember { mutableStateOf(0) } // 0 menu, 1 dang choi, 2 ket qua
    var kieu by remember { mutableStateOf(0) }
    var max by remember { mutableStateOf(10) }

    var cauSo by remember { mutableStateOf(1) }
    var diem by remember { mutableStateOf(0) }
    var cau by remember { mutableStateOf(taoCauHoi(0, 10)) }
    var dsDapAn by remember { mutableStateOf(listOf<Int>()) }
    var thongBao by remember { mutableStateOf("") }

    fun cauMoi() {
        cau = taoCauHoi(kieu, max)
        dsDapAn = taoDapAn(cau.dapAn, max)
    }

    fun batDau() {
        cauSo = 1
        diem = 0
        thongBao = ""
        cauMoi()
        man = 1
    }

    fun chonDapAn(x: Int) {
        if (x == cau.dapAn) {
            diem++
            thongBao = "Đúng rồi! Giỏi quá"
        } else {
            thongBao = "Sai rồi, đáp án đúng là ${cau.dapAn}"
        }

        if (cauSo == TONG_SO_CAU) {
            man = 2
        } else {
            cauSo++
            cauMoi()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (man == 0) {
            Text("Toán lớp 1", fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            Text("Chọn phép tính:", fontSize = 20.sp)
            Spacer(Modifier.height(8.dp))
            NutChon("Phép cộng", kieu == 0) { kieu = 0 }
            NutChon("Phép trừ", kieu == 1) { kieu = 1 }
            NutChon("Cộng và trừ", kieu == 2) { kieu = 2 }

            Spacer(Modifier.height(24.dp))
            Text("Chọn phạm vi:", fontSize = 20.sp)
            Spacer(Modifier.height(8.dp))
            NutChon("Trong phạm vi 10", max == 10) { max = 10 }
            NutChon("Trong phạm vi 20", max == 20) { max = 20 }

            Spacer(Modifier.height(32.dp))
            Button(onClick = { batDau() }) {
                Text("Bắt đầu", fontSize = 22.sp)
            }
        } else if (man == 1) {
            Text("Câu $cauSo / $TONG_SO_CAU", fontSize = 20.sp)
            Text("Điểm: $diem", fontSize = 20.sp)
            Spacer(Modifier.height(32.dp))

            Text("${cau.a} ${cau.dau} ${cau.b} = ?", fontSize = 56.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))

            for (x in dsDapAn) {
                Button(
                    onClick = { chonDapAn(x) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("$x", fontSize = 28.sp)
                }
            }

            Spacer(Modifier.height(16.dp))
            if (thongBao != "") {
                val mau = if (thongBao.startsWith("Đúng")) Color(0xFF2E7D32) else Color.Red
                Text(thongBao, color = mau, fontSize = 18.sp)
            }
        } else {
            Text("Hết giờ rồi!", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Text("Con làm đúng $diem / $TONG_SO_CAU câu", fontSize = 24.sp)
            Spacer(Modifier.height(8.dp))

            val loiKhen = when {
                diem == TONG_SO_CAU -> "Tuyệt vời, điểm 10!"
                diem >= 8 -> "Giỏi lắm!"
                diem >= 5 -> "Khá tốt, cố gắng thêm nhé"
                else -> "Luyện tập thêm nha"
            }
            Text(loiKhen, fontSize = 20.sp)

            Spacer(Modifier.height(32.dp))
            Button(onClick = { batDau() }) {
                Text("Chơi lại")
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { man = 0 }) {
                Text("Về menu")
            }
        }
    }
}

@Composable
fun NutChon(chu: String, daChon: Boolean, onClick: () -> Unit) {
    val mau = if (daChon) {
        ButtonDefaults.buttonColors()
    } else {
        ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        )
    }
    Button(
        onClick = onClick,
        colors = mau,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text(chu, fontSize = 18.sp)
    }
}