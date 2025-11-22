package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class EnquiryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etMessage: EditText
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(R.layout.activity_enqiury)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon?.setTint(resources.getColor(android.R.color.white))

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

        // Form elements
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etMessage = findViewById(R.id.etMessage)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            submitEnquiry()
        }
    }

    private fun submitEnquiry() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val message = etMessage.text.toString().trim()

        // Get selected courses
        val courses = mutableListOf<String>()
        val checkBoxes = listOf(
            findViewById<CheckBox>(R.id.cbFirstAid),
            findViewById<CheckBox>(R.id.cbSewing),
            findViewById<CheckBox>(R.id.cbLandscaping),
            findViewById<CheckBox>(R.id.cbLifeSkills),
            findViewById<CheckBox>(R.id.cbChildMinding),
            findViewById<CheckBox>(R.id.cbCooking),
            findViewById<CheckBox>(R.id.cbGarden)
        )

        for (cb in checkBoxes) {
            if (cb.isChecked) courses.add(cb.text.toString())
        }

        val selectedCourses = courses.joinToString(", ")

        // Validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || selectedCourses.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields and select at least one course", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare email intent
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("info@empoweringthenation.co.za"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Course Enquiry: $selectedCourses")
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Name: $name\nEmail: $email\nPhone: $phone\nCourses: $selectedCourses\nMessage: $message"
        )

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Enquiry via:"))
        } catch (e: Exception) {
            Toast.makeText(this, "No email client found.", Toast.LENGTH_SHORT).show()
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
