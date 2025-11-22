package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class CoursesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        // Toolbar setup
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon?.setTint(resources.getColor(android.R.color.black))

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = resources.getColor(android.R.color.black)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        // Buttons & Layouts
        val imageButtonSixMonth = findViewById<ImageButton>(R.id.imageButtonSixMonth)
        val imageButtonSixWeek = findViewById<ImageButton>(R.id.imageButtonSixWeek)
        val sixMonthLayout = findViewById<LinearLayout>(R.id.sixMonthCoursesLayout)
        val sixWeekLayout = findViewById<LinearLayout>(R.id.sixWeekCoursesLayout)

        val cbFirstAid = findViewById<CheckBox>(R.id.cbFirstAid)
        val cbSewing = findViewById<CheckBox>(R.id.cbSewing)
        val cbLandscaping = findViewById<CheckBox>(R.id.cbLandscaping)
        val cbLifeSkills = findViewById<CheckBox>(R.id.cbLifeSkills)
        val cbChildMinding = findViewById<CheckBox>(R.id.cbChildMinding)
        val cbCooking = findViewById<CheckBox>(R.id.cbCooking)
        val cbGarden = findViewById<CheckBox>(R.id.cbGarden)

        val btnProceed = findViewById<Button>(R.id.btnProceed)
        val backButton = findViewById<Button>(R.id.backButton)
        val btnEnquire = findViewById<Button>(R.id.btnEnquire)

        // Toggle course lists
        imageButtonSixMonth.setOnClickListener {
            sixMonthLayout.visibility = if (sixMonthLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        imageButtonSixWeek.setOnClickListener {
            sixWeekLayout.visibility = if (sixWeekLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        // Proceed to Payment
        btnProceed.setOnClickListener {
            val selected = arrayListOf<String>()
            if (cbFirstAid.isChecked) selected.add("First Aid")
            if (cbSewing.isChecked) selected.add("Sewing")
            if (cbLandscaping.isChecked) selected.add("Landscaping")
            if (cbLifeSkills.isChecked) selected.add("Life Skills")
            if (cbChildMinding.isChecked) selected.add("Child Minding")
            if (cbCooking.isChecked) selected.add("Cooking")
            if (cbGarden.isChecked) selected.add("Garden Maintenance")

            val intent = Intent(this, PaymentActivity::class.java)
            intent.putStringArrayListExtra("selectedCourses", selected)
            startActivity(intent)
        }

        // Enquire about a course
        btnEnquire.setOnClickListener {
            startActivity(Intent(this, EnquiryActivity::class.java))
        }

        // Back button
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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
