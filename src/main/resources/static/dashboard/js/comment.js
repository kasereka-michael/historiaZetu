$(document).ready(function() {
    // Handle toggling the reply form
    $('.reply').click(function(e) {
        e.preventDefault();
        let replyBox = $(this).closest('.media-body').find('.reply-box');

        // Clear form fields
        replyBox.find('textarea[name="commentContent"]').val('');
        replyBox.find('input[name="commentAuthor"]').val('');
        replyBox.find('input[name="commenterEmail"]').val('');

        replyBox.toggleClass('d-none');
    });

    // Handle form submission for both parent and nested comments
    $('.commentForm').submit(function(e) {
        e.preventDefault();

        // Get the specific form that was submitted
        var form = $(this);

        // Collect form data
        var formData = {
            commentContent: form.find('textarea[name="commentContent"]').val(),
            commentAuthor: form.find('input[name="commentAuthor"]').val(),
            commenterEmail: form.find('input[name="commenterEmail"]').val(),
            parentCommentId: form.find('input[name="parentCommentId"]').val() || null // Null for parent comments
        };

        // Validate the form fields
        if (!validateForm(formData, form)) {
            return; // Stop submission if validation fails
        }

        // Submit form data via AJAX
        $.ajax({
            type: 'POST',
            url: '/api/histories/submit-comment',
            data: formData,
            success: function(response) {
                alert('Comment submitted successfully!');
                $('.commentForm')[0].reset();
                location.reload(); // Reload to refresh comments
            },
            error: function(xhr, status, error) {
                console.error('Error Details:', xhr, status, error);
                alert('Error submitting comment: ');
            }
        });
    });

    // Validation function
    function validateForm(data, form) {
        let isValid = true;
        let errorMessage = '';

        // Clear previous outlines
        form.find('input, textarea').css('outline', 'none'); // Reset outline for all inputs

        // Check if comment content is provided
        if (!data.commentContent.trim()) {
            errorMessage += 'Comment content is required.\n';
            isValid = false;
            form.find('textarea[name="commentContent"]').css('outline', '2px solid red'); // Set red outline
        }

        // Check if comment author is provided
        if (!data.commentAuthor.trim()) {
            errorMessage += 'Your name is required.\n';
            isValid = false;
            form.find('input[name="commentAuthor"]').css('outline', '2px solid red'); // Set red outline
        }

        // Check if a valid email is provided
        if (!data.commenterEmail.trim() || !validateEmail(data.commenterEmail)) {
            errorMessage += 'A valid email is required.\n';
            isValid = false;
            form.find('input[name="commenterEmail"]').css('outline', '2px solid red'); // Set red outline
        }

        // If validation failed, show alert with error messages
        if (!isValid) {
            alert(errorMessage);
        }

        return isValid;
    }

    // Email validation regex
    function validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
});
