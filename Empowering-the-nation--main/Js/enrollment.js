// Course data
const courses = [
  {
    id: 1,
    title: "Professional Housekeeping",
    description: "Learn advanced cleaning techniques, organization skills, and professional standards for domestic work.",
    duration: "6 weeks",
    level: "Beginner",
    price: 299,
    image: "https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=300&h=200&fit=crop",
    features: ["Deep cleaning techniques", "Time management", "Safety protocols", "Customer service"]
  },
  {
    id: 2,
    title: "Garden Design & Maintenance",
    description: "Comprehensive gardening course covering plant care, landscape design, and sustainable practices.",
    duration: "8 weeks",
    level: "Intermediate",
    price: 399,
    image: "https://images.unsplash.com/photo-1416879595882-3373a0480b5b?w=300&h=200&fit=crop",
    features: ["Plant identification", "Landscape design", "Irrigation systems", "Organic farming"]
  },
  {
    id: 3,
    title: "Business Management Basics",
    description: "Essential business skills for starting your own cleaning or gardening service.",
    duration: "4 weeks",
    level: "Beginner",
    price: 249,
    image: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&h=200&fit=crop",
    features: ["Business planning", "Financial management", "Marketing basics", "Customer relations"]
  },
  {
    id: 4,
    title: "Advanced Cooking & Nutrition",
    description: "Master culinary skills and nutrition knowledge for private chef opportunities.",
    duration: "10 weeks",
    level: "Advanced",
    price: 499,
    image: "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=300&h=200&fit=crop",
    features: ["Culinary techniques", "Nutrition planning", "Food safety", "Menu development"]
  },
  {
    id: 5,
    title: "Elderly Care & Support",
    description: "Compassionate care techniques and health support for senior citizens.",
    duration: "6 weeks",
    level: "Beginner",
    price: 349,
    image: "https://images.unsplash.com/photo-1559757148-5c350d0d3c56?w=300&h=200&fit=crop",
    features: ["Health monitoring", "Mobility assistance", "Medication management", "Emotional support"]
  },
  {
    id: 6,
    title: "Professional Communication",
    description: "Develop excellent communication skills for better job opportunities.",
    duration: "3 weeks",
    level: "Beginner",
    price: 199,
    image: "https://images.unsplash.com/photo-1553484771-371a605b060b?w=300&h=200&fit=crop",
    features: ["English communication", "Professional etiquette", "Conflict resolution", "Interview skills"]
  }
];

// State
let cart = [];
let isCheckoutMode = false;

// DOM elements
const courseSelect = document.getElementById('courseSelect');
const addToCartBtn = document.getElementById('addToCartBtn');
const coursesGrid = document.getElementById('coursesGrid');
const cartItems = document.getElementById('cartItems');
const cartCount = document.getElementById('cartCount');
const cartSummary = document.getElementById('cartSummary');
const checkoutBtn = document.getElementById('checkoutBtn');
const mainContent = document.getElementById('mainContent');
const checkoutContent = document.getElementById('checkoutContent');
const backToCartBtn = document.getElementById('backToCartBtn');
const checkoutForm = document.getElementById('checkoutForm');
const paymentMethod = document.getElementById('paymentMethod');
const cardFields = document.getElementById('cardFields');
const completePaymentBtn = document.getElementById('completePaymentBtn');
const successModal = document.getElementById('successModal');
const mobileMenuBtn = document.getElementById('mobileMenuBtn');
const mobileMenu = document.getElementById('mobileMenu');

// Initialize
function init() {
  populateCourseSelect();
  renderCoursesGrid();
  setupEventListeners();
  updateCart();
}

// Populate course select dropdown
function populateCourseSelect() {
  courses.forEach(course => {
    const option = document.createElement('option');
    option.value = course.id;
    option.textContent = `${course.title} - $${course.price}`;
    courseSelect.appendChild(option);
  });
}

// Render courses grid
function renderCoursesGrid() {
  coursesGrid.innerHTML = courses.map(course => `
    <div class="card hover:shadow-lg transition-shadow">
      <div class="relative">
        <img src="${course.image}" alt="${course.title}" class="w-full h-32 object-cover rounded-t-lg">
        <span class="badge badge-primary absolute top-2 right-2">$${course.price}</span>
      </div>
      <div class="p-4">
        <h3 class="font-semibold text-gray-900 mb-2">${course.title}</h3>
        <div class="flex gap-2 mb-3">
          <span class="badge badge-outline">${course.level}</span>
          <span class="badge badge-outline">${course.duration}</span>
        </div>
        <p class="text-sm text-gray-600 mb-3">${course.description}</p>
        <button onclick="addToCart(${course.id})" class="w-full btn-primary">
          Add to Cart
        </button>
      </div>
    </div>
  `).join('');
}

// Setup event listeners
function setupEventListeners() {
  courseSelect.addEventListener('change', () => {
    addToCartBtn.disabled = !courseSelect.value;
  });

  addToCartBtn.addEventListener('click', () => {
    if (courseSelect.value) {
      addToCart(parseInt(courseSelect.value));
      courseSelect.value = '';
      addToCartBtn.disabled = true;
    }
  });

  checkoutBtn.addEventListener('click', showCheckout);
  backToCartBtn.addEventListener('click', showCart);

  paymentMethod.addEventListener('change', () => {
    const isCardPayment = paymentMethod.value.includes('card');
    cardFields.style.display = isCardPayment ? 'block' : 'none';
    updateCheckoutButton();
  });

  checkoutForm.addEventListener('submit', handlePaymentSubmit);

  mobileMenuBtn.addEventListener('click', () => {
    mobileMenu.classList.toggle('hidden');
  });

  document.getElementById('viewStatusBtn').addEventListener('click', () => {
    alert('Redirecting to application status page...');
  });

  document.getElementById('backToHomeBtn').addEventListener('click', () => {
    window.location.href = 'index.html';
  });
}

// Add course to cart
function addToCart(courseId) {
  const course = courses.find(c => c.id === courseId);
  if (!course) return;

  const existingItem = cart.find(item => item.course.id === courseId);
  if (existingItem) {
    existingItem.quantity += 1;
  } else {
    cart.push({ course, quantity: 1 });
  }
  updateCart();
}

// Remove from cart
function removeFromCart(courseId) {
  cart = cart.filter(item => item.course.id !== courseId);
  updateCart();
}

// Update quantity
function updateQuantity(courseId, newQuantity) {
  if (newQuantity <= 0) {
    removeFromCart(courseId);
    return;
  }
  const item = cart.find(item => item.course.id === courseId);
  if (item) {
    item.quantity = newQuantity;
    updateCart();
  }
}

// Calculate totals
function getTotalPrice() {
  return cart.reduce((total, item) => total + (item.course.price * item.quantity), 0);
}

function getDiscountedPrice() {
  const total = getTotalPrice();
  const discount = cart.length >= 3 ? 0.15 : cart.length >= 2 ? 0.1 : 0;
  return total * (1 - discount);
}

function getDiscountAmount() {
  return getTotalPrice() - getDiscountedPrice();
}

// Update cart display
function updateCart() {
  cartCount.textContent = cart.length;

  if (cart.length === 0) {
    cartItems.innerHTML = '<p class="text-gray-500 text-center py-8">Your cart is empty</p>';
    cartSummary.classList.add('hidden');
    return;
  }

  cartItems.innerHTML = cart.map(item => `
    <div class="border rounded-lg p-3 cart-item">
      <div class="flex justify-between items-start mb-2">
        <h4 class="text-sm font-medium">${item.course.title}</h4>
        <button onclick="removeFromCart(${item.course.id})" class="text-red-600 hover:text-red-700">🗑️</button>
      </div>
      <div class="flex justify-between items-center">
        <div class="flex items-center gap-2 quantity-controls">
          <button onclick="updateQuantity(${item.course.id}, ${item.quantity - 1})">-</button>
          <span class="w-8 text-center text-sm">${item.quantity}</span>
          <button onclick="updateQuantity(${item.course.id}, ${item.quantity + 1})">+</button>
        </div>
        <p class="text-sm font-medium">$${(item.course.price * item.quantity).toFixed(2)}</p>
      </div>
    </div>
  `).join('');

  // Discount alert
  const discountAlert = document.getElementById('discountAlert');
  const discountText = document.getElementById('discountText');
  if (cart.length >= 2) {
    discountAlert.classList.remove('hidden');
    discountText.textContent = `Bundle Discount Applied! ${cart.length >= 3 ? '15% off' : '10% off'} for multiple courses.`;
  } else {
    discountAlert.classList.add('hidden');
  }

  const subtotal = getTotalPrice();
  const discountAmount = getDiscountAmount();
  const total = getDiscountedPrice();

  document.getElementById('subtotal').textContent = `$${subtotal.toFixed(2)}`;
  document.getElementById('total').textContent = `$${total.toFixed(2)}`;

  if (discountAmount > 0) {
    document.getElementById('discountRow').classList.remove('hidden');
    document.getElementById('discountAmount').textContent = `-$${discountAmount.toFixed(2)}`;
  } else {
    document.getElementById('discountRow').classList.add('hidden');
  }

  cartSummary.classList.remove('hidden');
}

// Checkout
function showCheckout() {
  isCheckoutMode = true;
  mainContent.classList.add('hidden');
  checkoutContent.classList.remove('hidden');

  const checkoutSummary = document.getElementById('checkoutSummary');
  checkoutSummary.innerHTML = cart.map(item => `
    <div class="flex justify-between items-center">
      <div>
        <p class="text-sm font-medium">${item.course.title}</p>
        <p class="text-xs text-gray-600">Qty: ${item.quantity}</p>
      </div>
      <p class="text-sm">$${(item.course.price * item.quantity).toFixed(2)}</p>
    </div>
  `).join('') + `
    <hr class="my-4">
    <div class="space-y-2">
      <div class="flex justify-between">
        <p>Subtotal:</p>
        <p>$${getTotalPrice().toFixed(2)}</p>
      </div>
      ${getDiscountAmount() > 0 ? `
      <div class="flex justify-between text-green-600">
        <p>Bundle Discount:</p>
        <p>-$${getDiscountAmount().toFixed(2)}</p>
      </div>` : ''}
      <hr class="my-2">
      <div class="flex justify-between text-lg font-semibold">
        <p>Total:</p>
        <p>$${getDiscountedPrice().toFixed(2)}</p>
      </div>
    </div>
  `;

  document.getElementById('checkoutTotal').textContent = getDiscountedPrice().toFixed(2);
}

function showCart() {
  isCheckoutMode = false;
  mainContent.classList.remove('hidden');
  checkoutContent.classList.add('hidden');
}

// Validate checkout
function updateCheckoutButton() {
  const isFormValid = paymentMethod.value &&
    document.getElementById('firstName').value &&
    document.getElementById('lastName').value &&
    document.getElementById('email').value &&
    document.getElementById('phone').value &&
    document.getElementById('address').value &&
    document.getElementById('city').value &&
    document.getElementById('zipCode').value;

  const isCardValid = !paymentMethod.value.includes('card') ||
    (document.getElementById('cardNumber').value &&
     document.getElementById('expiryDate').value &&
     document.getElementById('cvv').value);

  completePaymentBtn.disabled = !(isFormValid && isCardValid);
}

// Payment submission
function handlePaymentSubmit(e) {
  e.preventDefault();

  setTimeout(() => {
    successModal.classList.remove('hidden');
    cart = [];
    updateCart();
  }, 1000);
}

function addFormValidation() {
  const formInputs = checkoutForm.querySelectorAll('input, select');
  formInputs.forEach(input => {
    input.addEventListener('input', updateCheckoutButton);
  });
}

document.addEventListener('DOMContentLoaded', () => {
  init();
  setTimeout(addFormValidation, 100);
});
