package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ContactActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        // Toolbar setup
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

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

        // Make addresses clickable
        makeParentsNotFocusable(findViewById(R.id.addressText1))
        makeParentsNotFocusable(findViewById(R.id.addressText2))
        makeParentsNotFocusable(findViewById(R.id.addressText3))

        setLinkClickable(findViewById(R.id.addressText1), "789 Republic Road, Randburg, 2194")
        setLinkClickable(findViewById(R.id.addressText2), "901 New Road, Midrand, 1685")
        setLinkClickable(findViewById(R.id.addressText3), "567 Cradock Avenue, Rosebank, 2196")

        // Phone click
        findViewById<TextView>(R.id.phoneText).setOnClickListener {
            Log.d("ContactActivity", "phoneText clicked")
            Toast.makeText(this@ContactActivity, "Opening dialer...", Toast.LENGTH_SHORT).show()
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+27102345679"))
            safeStartActivity(dialIntent)
        }

        // ✅ Updated social media buttons (works on all phones)
        findViewById<ImageButton>(R.id.instagramIcon).setOnClickListener {
            openSocialLinkAppFirst(
                appUri = "instagram://user?username=empowering_the_nation01",
                webUrl = "https://www.instagram.com/empowering_the_nation01/"
            )
        }

        findViewById<ImageButton>(R.id.twitterIcon).setOnClickListener {
            openSocialLinkAppFirst(
                appUri = "twitter://user?screen_name=EmpoweringNa",
                webUrl = "https://x.com/EmpoweringNa"
            )
        }

        findViewById<ImageButton>(R.id.facebookIcon).setOnClickListener {
            openSocialLinkAppFirst(
                appUri = "fb://facewebmodal/f?href=https://www.facebook.com/share/172ugeDSQe/",
                webUrl = "https://www.facebook.com/share/172ugeDSQe/"
            )
        }
    }

    // Prevent parent views from blocking clicks
    private fun makeParentsNotFocusable(view: View?) {
        var p = view?.parent
        while (p is ViewGroup) {
            p.isFocusable = false
            p.isClickable = false
            p = p.parent
        }
    }

    // Address click opens maps
    private fun setLinkClickable(tv: TextView, location: String) {
        tv.isClickable = true
        tv.isFocusable = false
        tv.setOnClickListener {
            Log.d("ContactActivity", "address clicked: $location")
            Toast.makeText(this@ContactActivity, "Opening map...", Toast.LENGTH_SHORT).show()
            openMapLocation(location)
        }
    }

    private fun openMapLocation(location: String) {
        val googleMapsWeb = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(location)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsWeb))
        safeStartActivity(intent)
    }

    // ✅ Updated universal open method for social media
    private fun openSocialLinkAppFirst(appUri: String, webUrl: String) {
        try {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appUri))
            if (appIntent.resolveActivity(packageManager) != null) {
                startActivity(appIntent)
            } else {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                startActivity(webIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to open link", Toast.LENGTH_SHORT).show()
            Log.e("ContactActivity", "Error opening social link: ${e.message}")
        }
    }

    private fun safeStartActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Log.w("ContactActivity", "No app available for intent: ${e.message}")
            Toast.makeText(this, "No application installed to handle this action", Toast.LENGTH_LONG).show()
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
