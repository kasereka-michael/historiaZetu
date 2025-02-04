function startCountdown(duration, display) {
    let timer = duration, minutes, seconds;
    const countdown = setInterval(function () {
        minutes = Math.floor(timer / 60);
        seconds = timer % 60;

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = minutes + ":" + seconds;

        if (--timer < 0) {
            clearInterval(countdown);
            display.textContent = "Time's up!";
        }
    }, 1000);
}

function timer() {
    const threeMinutes = 60 * 3;
    const display = document.querySelector('.timer');
    startCountdown(threeMinutes, display);
};




document.getElementById('submit-btn').addEventListener('click', function () {
    const email = document.getElementById('username').value.trim();
    document.getElementById('submit-icon').classList.remove('d-none');
    fetch('/api/v1/recover-password/email', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                timer();
                document.getElementById('info-text').classList.add('d-none');
                document.getElementById('otp-group').classList.remove('d-none');
                document.getElementById('submit-btn').classList.add('d-none');
                document.getElementById('check-code-btn').classList.remove('d-none');
            } else {

                document.getElementById('info-text').innerHTML = 'Failed to send recovery email';
                document.getElementById('submit-icon').classList.remove('d-none');
            }
        })
        .catch(error => {

            document.getElementById('info-text').innerHTML = 'An error occurred. Please try again.';
            document.getElementById('submit-icon').classList.remove('d-none');
        });
});

document.getElementById('check-code-btn').addEventListener('click', function () {
    const email = document.getElementById('username').value.trim();
    const otp = document.getElementById('otp').value.trim();

    fetch('/api/v1/recover-password/check-otp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, otp })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                document.getElementById('info-text').classList.add('d-none');
                document.getElementById('otp-group').classList.add('d-none');
                document.getElementById('password-group').classList.remove('d-none');
                document.getElementById('password-confirm-group').classList.remove('d-none');
                document.getElementById('check-code-btn').classList.add('d-none');
                document.getElementById('update-pass-btn').classList.remove('d-none');
            } else {
                document.getElementById('info-text').classList.remove('d-none');
                document.getElementById('info-text').innerHTML = 'Invalid OTP';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('info-text').classList.remove('d-none');
            document.getElementById('info-text').innerHTML = 'an error occurred please try again';
        });
});




document.getElementById('resend-code-btn').addEventListener('click', function (e) {
    e.preventDefault();
    const email = document.getElementById('username').value.trim();

    fetch('/api/v1/recover-password/resend-otp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                timer();
                document.getElementById('info-text').classList.add('d-none');

            } else {
                document.getElementById('info-text').classList.remove('d-none');
                document.getElementById('info-text').innerHTML = 'Failed to Reproduced an new otp';
                document.getElementById('submit-icon').classList.remove('d-none');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('info-text').classList.remove('d-none');
            document.getElementById('info-text').innerHTML = 'an error occurred please try again';
            document.getElementById('submit-icon').classList.remove('d-none');
        });
});






document.getElementById('update-pass-btn').addEventListener('click', function () {

    let email = document.getElementById('username').value.trim();
    let password = document.getElementById('password').value.trim();
    const confirmPassword = document.getElementById('password-confirm').value;

    email = email.trim();
    password = password.trim();
    if (password !== confirmPassword) {
        document.getElementById('info-text').innerHTML = 'Passwords do not match';
        return;
    }

    fetch('/api/v1/recover-password/update-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                console.log('Password updated successfully'+ data.status);
                toastr.success('Password updated successfully');
                document.getElementById('return-login').classList.remove('d-none');
                document.getElementById('info-text').classList.add('d-none');
                document.getElementById('password-recovery-form').classList.add('d-none');
                document.getElementById('operation-completed').classList.remove('d-none');
            } else {
                console.log('Password updated successfully');
                toastr.error('Could not update password');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('info-text').classList.remove('d-none')
            document.getElementById('info-text').innerHTML = 'an error occurred please try again' + error.message;
        });
});