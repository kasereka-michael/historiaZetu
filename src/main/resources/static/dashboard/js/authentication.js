$(document).ready(function () {
   $('#otp-form').hide();
    $('#signupBtn').click(function (event) {
        event.preventDefault();

        const otpForm = $('#otp-form').hide();
        const signupForm = $('#signup');
        const errorMessage = $('#error-message').text('');

        // Get input values
        const email = $('#email').val().trim();
        const phone = $('#phone').val().trim();
        const password = $('#password').val().trim();
        const confirmPassword = $('#confirmPassword').val().trim();

        // Frontend validation
        if (!email || !phone || !password || !confirmPassword) {
            errorMessage.text('All fields are required!');
            return;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            errorMessage.text('Please enter a valid email address!');
            return;
        }

        const phoneRegex = /^\+?[1-9]\d{1,14}$/; // E.164 format
        if (!phoneRegex.test(phone)) {
            errorMessage.text('Phone number must start with the country code (e.g., +1234567890)!');
            return;
        }

        if (password.length < 8) {
            errorMessage.text('Password must be at least 8 characters long!');
            return;
        }

        if (password !== confirmPassword) {
            errorMessage.text('Passwords do not match!');
            return;
        }

        // Send data to the backend
        $.ajax({
            url: '/api/signup', // Backend endpoint
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email,
                phoneNumber: phone,
                password: password,
            }),
            success: function (response) {
                console.log("response :: " + response)

                console.log("data :: " + this.data)
                $('#userId').val(response);
                console.log("user id is here now " + response);
                signupForm[0].reset();
                otpForm.show();
                signupForm.hide();
            },
            error: function (xhr) {
                const errorMessage = xhr.responseJSON?.message || xhr.responseText || 'Failed to create account.';
                alert('Error: ' + errorMessage);
            },
        });
    });




      // otp backend

      $('#otpBtn').click(function (event) {
        event.preventDefault();

        // Get user ID
        const userId = $('#userId').val().trim();
        if (!userId) {
            alert('User ID is missing!');
            return;
        }

        // Get OTP input values
        let otp = '';
        $('.input').each(function () {
            otp += $(this).val().trim();
        });

        // Validate OTP (e.g., length)
        if (otp.length !== 6 || isNaN(otp)) {
            alert('Please enter a valid 6-digit OTP.');
            return;
        }

        // Send OTP to backend
        $.ajax({
            url: '/api/verify-otp', // Backend endpoint for OTP verification
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                users: {id: userId},
                otp: otp,
            }),
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Account activation',
                    html: '<b>Great job!</b> Your account has been activated.',
                    confirmButtonText: 'Continue',
                    confirmButtonColor: '#3a6873',
                    background: '#f7f9fc',
                    timer: 4000
                });
                window.location.href = '/authentication';
            },
            error: function (xhr) {
                alert('Error: ' + (xhr.responseText || 'OTP verification failed.'));
            },
        });
    });

//     opt validation




});
