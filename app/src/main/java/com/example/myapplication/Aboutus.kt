package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Aboutus : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutus)

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon?.setTint(resources.getColor(android.R.color.black))


        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        // Drawer toggle setup
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = resources.getColor(android.R.color.black)
        navigationView.setNavigationItemSelectedListener(this)

        // Next button
        val nextButton: Button = findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            val intent = Intent(this, CoursesActivity::class.java)
            startActivity(intent)
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
