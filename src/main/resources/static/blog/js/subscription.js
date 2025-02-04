$(document).ready(function () {

    const subscriptionForms = $(".form-subscription");

    // Check if the user has already subscribed
    const savedEmail = localStorage.getItem("subscribedEmail"); // Globally save email in localStorage
    if (savedEmail) {
        checkSubscriptionStatus(savedEmail);
    }

    // Iterate through all subscription forms
    subscriptionForms.each(function () {
        const subscriptionForm = $(this);
        const emailInput = subscriptionForm.find("input[type='email']");

        // Handle form submission
        subscriptionForm.on("submit", function (event) {
            event.preventDefault();

            const email = emailInput.val();

            if (email) {
                // Save the email in localStorage
                localStorage.setItem("subscribedEmail", email);

                // Submit the email to the backend for subscription
                submitSubscription(email);
            } else {
                alert("Please enter a valid email address.");
            }
        });
    });

    // Function to check subscription status
    function checkSubscriptionStatus(email) {
        $.ajax({
            url: "/api/subscription/status", // Backend endpoint to check subscription
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ email: email }), // Send email to the backend
            success: function (response) {
                if (response.isSubscribed) {
                    // Hide all forms globally if subscribed
                    $(".form-subscription").hide();

                } else {
                    localStorage.removeItem("subscribedEmail");
                }
            },
            error: function () {
                console.log("Failed to check subscription status. Please try again later.");
            }
        });
    }

    // Function to handle subscription submission
    function submitSubscription(email) {
        $.ajax({
            url: "/api/subscription/submit", // Backend endpoint to handle subscription
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ email: email }), // Send email to the backend
            success: function (response) {
                if (response.success) {
                    // Hide all forms globally
                    $(".form-subscription").hide();
                    Swal.fire({
                        icon: 'success',
                        title: 'Subscription Successful',
                        html: '<b>Great job!</b> we will keep updated with the latest news',
                        confirmButtonText: 'Ok!',
                        confirmButtonColor: '#3a6873',
                        background: '#f7f9fc',
                        timer: 4000
                    });
                } else {
                    alert("Failed to subscribe. Please try again.");
                }
            },
            error: function () {
                alert("Failed to submit subscription. Please try again later.");
            }
        });
    }
});
