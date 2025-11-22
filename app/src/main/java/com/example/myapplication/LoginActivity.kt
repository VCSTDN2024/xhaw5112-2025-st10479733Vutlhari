package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class LoginActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(R.layout.activity_login)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon?.setTint(resources.getColor(android.R.color.black))

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = resources.getColor(android.R.color.black)
        navigationView.setNavigationItemSelectedListener(this)

        val emailEditText = findViewById<EditText>(R.id.usernameedtTxt)
        val passwordEditText = findViewById<EditText>(R.id.passwordedtText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<TextView>(R.id.signupButton)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)

        val sharedPref = getSharedPreferences("UserPref", MODE_PRIVATE)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val savedEmail = sharedPref.getString("email", null)
            val savedPassword = sharedPref.getString("password", null)

            if (email == savedEmail && password == savedPassword) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CoursesActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        forgotPassword.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email to reset your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedEmail = sharedPref.getString("email", null)

            if (email == savedEmail) {
                val tempPassword = "Temp@123"
                sharedPref.edit().putString("password", tempPassword).apply()

                Toast.makeText(
                    this,
                    "Password reset! Use temporary password: $tempPassword",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this, "Email not found. Please check and try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_main -> startActivity(Intent(this, MainActivity::class.java))
            R.id.nav_login -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.nav_signup -> startActivity(Intent(this, SignupActivity::class.java))
            R.id.nav_about_us -> startActivity(Intent(this, Aboutus::class.java))
            R.id.nav_contact -> startActivity(Intent(this, ContactActivity::class.java))
            R.id.nav_course -> startActivity(Intent(this, CoursesActivity::class.java))
            R.id.nav_enquiry -> startActivity(Intent(this, EnquiryActivity::class.java))
            R.id.nav_payment -> startActivity(Intent(this, PaymentActivity::class.java))
        }
        drawerLayout.closeDrawers()
        return true
    }
}
