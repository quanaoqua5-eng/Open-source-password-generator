package com.passdrop.passwordgen

import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.passdrop.passwordgen.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
    
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ev.flags and MotionEvent.FLAG_WINDOW_IS_OBSCURED != 0) {
                return false
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}

