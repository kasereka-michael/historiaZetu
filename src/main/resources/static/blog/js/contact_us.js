$(document).ready(function () {
    $("#contact-form").on("submit", function (event) {
        event.preventDefault();  // Prevent form from reloading the page

        // Collect form data
        const name = $("#name").val();
        const email = $("#email").val();
        const message = $("#message").val();

        // Create an object with the form data
        const formData = {
            name: name,
            email: email,
            message: message
        };

        // Send data to the backend via AJAX
        $.ajax({
            url: '/api/contactus',  // URL of the backend endpoint
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Message sent Successful',
                    html: '<b>Thank you for your message!</b> We will get back to you soon.',
                    confirmButtonText: 'Ok!',
                    confirmButtonColor: '#3a6873',
                    background: '#f7f9fc',
                    timer: 4000
                });
                // Optionally, reset the form
                $("#contact-form")[0].reset();
            },
            error: function (error) {
                console.log("There was an error submitting your message. Please try again.");
            }
        });
    });
});
