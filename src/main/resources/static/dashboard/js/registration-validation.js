document.addEventListener('DOMContentLoaded', function() {
    const errorSpan = document.getElementById('errorMessage');
    if(errorSpan.innerHTML === '') {
      errorSpan.style.display = 'none';
    }
    else {
        errorSpan.style.display = 'block';
    }
    const form = document.querySelector('form');
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        if (validateForm()) {
            sendData();
        }
    });

    function validateForm() {
        const email = document.getElementById('useremail').value;
        const firstName = document.getElementById('userfirstname').value;
        const lastName = document.getElementById('userlastname').value;
        const phone = document.getElementById('userphone').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('passwordconfirm').value;

        if (!email || !firstName || !lastName || !phone || !password || !confirmPassword) {
            errorSpan.style.display = 'block';
            showError('All fields are required');
            return false;
        }

        if (!isValidEmail(email)) {
            errorSpan.style.display = 'block';
            showError('Invalid email format');
            return false;
        }

        if (password.length < 8 || password.length > 12) {
            errorSpan.style.display = 'block';
            showError('Password must be between 8 and 12 characters');
            return false;
        }

        if (password !== confirmPassword) {
            errorSpan.style.display = 'block';
            showError('Passwords do not match');
            return false;
        }

        return true;
    }

    function isValidEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    function showError(message) {
        const errorSpan = document.getElementById('errorMessage');
        errorSpan.style.display = 'block';
        errorSpan.textContent = message;

        // Set a timeout to hide the error message after 5 seconds
        setTimeout(() => {
            errorSpan.style.display = 'none';
        }, 5000);
    }


    function sendData() {
        const formData = {
            userEmail: document.getElementById('useremail').value,
            firstName: document.getElementById('userfirstname').value,
            lastName: document.getElementById('userlastname').value,
            phoneNumber: document.getElementById('userphone').value,
            userPassword: document.getElementById('password').value
        };

        fetch('/api/v1/users/registration/admin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Response:', data.httpStatus);
                if (data.createdUser !== null) {
                    form.reset();
                    errorSpan.style.display = 'none';
                    toastr.success('Registration successful');
                } else {
                    errorSpan.style.display = 'none';
                    toastr.error(data.message || 'Registration failed');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                errorSpan.style.display = 'block';
                showError('An error occurred. Please try again. ' + error);
            });
    }
});
