package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager // 👈🏽 add this import
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class PaymentActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private val coursePrices = mapOf(
        "First Aid" to 1500,
        "Sewing" to 1500,
        "Landscaping" to 1500,
        "Life Skills" to 1500,
        "Child Minding" to 750,
        "Cooking" to 750,
        "Garden Maintenance" to 750
    )

    private val coursePurpose = mapOf(
        "First Aid" to "First aid awareness & basic life support (CPR, emergencies).",
        "Sewing" to "Alterations & tailoring (stitches, threading, zips, seams).",
        "Landscaping" to "Landscaping for new/established gardens (plants, structures).",
        "Life Skills" to "Banking, basic labour law, literacy & numeracy.",
        "Child Minding" to "Baby & child care: birth–1 year, toddlers, educational toys.",
        "Cooking" to "Nutritious meals: nutrition, planning, recipes, preparation.",
        "Garden Maintenance" to "Watering, pruning, propagation, planting techniques."
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 👇🏽 Add this line to make screen recording work
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)

        // --- Navigation Drawer Setup ---
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

        // --- Payment Logic ---
        val tvSummary = findViewById<TextView>(R.id.tvSummary)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val btnGetQuote = findViewById<Button>(R.id.btnGetQuote)
        val btnPayNow = findViewById<Button>(R.id.btnPayNow)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val backButton = findViewById<Button>(R.id.backButton)

        val paymentFormContainer = findViewById<LinearLayout>(R.id.paymentFormContainer)
        val cardPaymentForm = findViewById<LinearLayout>(R.id.cardPaymentForm)
        val eftPaymentForm = findViewById<LinearLayout>(R.id.eftPaymentForm)
        val btnSubmitPayment = findViewById<Button>(R.id.btnSubmitPayment)

        val selected = intent.getStringArrayListExtra("selectedCourses") ?: arrayListOf()

        var subtotal = 0.0
        val details = StringBuilder("Selected courses:\n\n")

        selected.forEach { course ->
            val price = coursePrices[course]?.toDouble() ?: 0.0
            subtotal += price
            val purpose = coursePurpose[course] ?: ""
            details.append("• $course — R${"%.2f".format(price)}\n   $purpose\n\n")
        }

        val discountRate = when (selected.size) {
            2 -> 0.05
            3 -> 0.10
            else -> if (selected.size >= 4) 0.15 else 0.0
        }

        val discountAmount = subtotal * discountRate
        val total = subtotal - discountAmount

        tvSummary.text = details.toString().trim()
        tvTotal.text = """
            Subtotal:   R${"%.2f".format(subtotal)}
            Discount:   -R${"%.2f".format(discountAmount)} (${(discountRate * 100).toInt()}%)
            -------------------------
            Total:      R${"%.2f".format(total)}
        """.trimIndent()

        // ===== GET A QUOTE BUTTON =====
        btnGetQuote.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Course Quote")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "${tvSummary.text}\n${tvTotal.text}")
            startActivity(Intent.createChooser(emailIntent, "Send quote using:"))
        }

        btnPayNow.setOnClickListener {
            val paymentOptions = arrayOf("Credit/Debit Card", "EFT/Bank Transfer")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose Payment Method")
            builder.setItems(paymentOptions) { _, which ->
                paymentFormContainer.visibility = LinearLayout.VISIBLE

                when (which) {
                    0 -> {
                        cardPaymentForm.visibility = LinearLayout.VISIBLE
                        eftPaymentForm.visibility = LinearLayout.GONE
                    }

                    1-> {
                        cardPaymentForm.visibility = LinearLayout.GONE
                        eftPaymentForm.visibility = LinearLayout.VISIBLE
                    }
                }
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }

        btnSubmitPayment.setOnClickListener {
            Toast.makeText(this, "Payment processed!", Toast.LENGTH_SHORT).show()
            paymentFormContainer.visibility = LinearLayout.GONE
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, CoursesActivity::class.java))
        }

        nextButton.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
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
