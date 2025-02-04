/**
 * Validates the fields in the first step of the application form.
 * @returns {boolean} True if all fields are valid, false otherwise.
 */
function validateStep1() {
    console.log('Next button clicked');
    const fields = [
        'username', 'fathername', 'mathername', 'citizenship', 'phonenumber',
        'useremail', 'placeofbirth', 'dob', 'handicapexplain', 'closer_username',
        'closer_relationship', 'closer_address'
    ];

    let isValid = true;

    fields.forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field && field.value.trim() === '') {
            isValid = false;
            field.classList.add('is-invalid');
        } else if (field) {
            field.classList.remove('is-invalid');
        }
    });

    // Validate email
    const emailField = document.getElementById('useremail');
    if (emailField && !isValidEmail(emailField.value)) {
        isValid = false;
        emailField.classList.add('is-invalid');
    }

    // Validate phone number
    const phoneField = document.getElementById('phonenumber');
    if (phoneField && !isValidPhoneNumber(phoneField.value)) {
        isValid = false;
        phoneField.classList.add('is-invalid');
    }

    // Validate sex (at least one checkbox should be checked)
    const maleCheckbox = document.getElementById('male');
    const femaleCheckbox = document.getElementById('female');
    if ((!maleCheckbox || !maleCheckbox.checked) && (!femaleCheckbox || !femaleCheckbox.checked)) {
        isValid = false;
        document.getElementById('sex').classList.add('is-invalid');
    } else {
        document.getElementById('sex').classList.remove('is-invalid');
    }

    // Validate handicap (at least one checkbox should be checked)
    const yesHandicap = document.getElementById('yes');
    const noHandicap = document.getElementById('no');
    if ((!yesHandicap || !yesHandicap.checked) && (!noHandicap || !noHandicap.checked)) {
        isValid = false;
        document.getElementById('handicap').classList.add('is-invalid');
    } else {
        document.getElementById('handicap').classList.remove('is-invalid');
    }

    return isValid;
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function isValidPhoneNumber(phone) {
    const phoneRegex = /^\+?[0-9\s]{10,14}$/;
    return phoneRegex.test(phone);
}

// Add event listener to the "Next" button
document.querySelector('.btn-navigate-form-step').addEventListener('click', function(e) {

    if (!validateStep1()) {
        e.preventDefault();
        toastr.error('Please in step 1 there is a mistake make sure all fields are correct');
    }
});



/**
 * Validates the fields in the second step of the application form.
 * @returns {boolean} True if all fields are valid, false otherwise.
 */
function validateStep2() {
    let isValid = true;

    // Check if either "Yes" or "No" is checked
    if (!document.getElementById('ye-s').checked && !document.getElementById('n-o').checked) {
        toastr.error("Please select Yes or No for post-secondary education");
        isValid = false;
    }

    // If "Yes" is checked, validate institution name
    if (document.getElementById('ye-s').checked) {
        let diplomaExplanation = document.getElementById('diploma-explaination').value.trim();
        if (diplomaExplanation === '') {
            toastr.error("Please enter the name of the institution");
            isValid = false;
        }
    }

    // Validate secondary schools attended
    let secondaryAttended = document.getElementById('secondary-attended').value.trim();
    if (secondaryAttended === '') {
        toastr.error("Please enter the secondary schools attended");
        isValid = false;
    }

    // Validate EHEECE Result
    let eheece = document.getElementById('eheece').value.trim();
    if (eheece === '') {
        toastr.error("Please enter your EHEECE Result");
        isValid = false;
    }

    // Validate ESLCE Result
    let eslce = document.getElementById('eslce').value.trim();
    if (eslce === '') {
        toastr.error("Please enter your ESLCE Result");
        isValid = false;
    }

    // Validate English Result
    let english = document.getElementById('english').value.trim();
    if (english === '') {
        toastr.error("Please enter your English Result");
        isValid = false;
    }

    // Validate Mathematics Result
    let mathematic = document.getElementById('mathematic').value.trim();
    if (mathematic === '') {
        toastr.error("Please enter your Mathematics Result");
        isValid = false;
    }

    // Validate Preparatory School Stream
    let preparatoryStream = document.getElementById('preparatory-stream').value.trim();
    if (preparatoryStream === '') {
        toastr.error("Please indicate your Preparatory School Stream");
        isValid = false;
    }

    return isValid;
}

document.querySelector('button[step_number="3"]').addEventListener('click', function(e) {
    if (!validateStep2()) {
        e.preventDefault(); // Prevent moving to next step if validation fails
        toastr.error('Please in step 2 there is a mistake make sure all fields are correct');
    }
});



document.addEventListener('DOMContentLoaded', function() {
    const yesEmployed = document.getElementById('yes-employed');
    const noEmployed = document.getElementById('no-employed');
    const employerName = document.getElementById('employer-name');
    const jobType = document.getElementById('job-type');



    // Initially hide the input fields
    employerName.parentElement.style.display = 'none';
    jobType.parentElement.style.display = 'none';

    // Toggle visibility of input fields based on checkbox selection
    yesEmployed.addEventListener('change', function() {
        if (this.checked) {
            employerName.parentElement.style.display = 'block';
            jobType.parentElement.style.display = 'block';
            noEmployed.checked = false;
        } else {
            employerName.parentElement.style.display = 'none';
            jobType.parentElement.style.display = 'none';
        }
    });

    noEmployed.addEventListener('change', function() {
        if (this.checked) {
            employerName.parentElement.style.display = 'none';
            jobType.parentElement.style.display = 'none';
            yesEmployed.checked = false;
        }
    });

    // Validation function
    function validateStep3() {
        let isValid = true;

        if (!yesEmployed.checked && !noEmployed.checked) {
            toastr.error("Please select whether you are currently employed or not.");
            isValid = false;
        }

        if (yesEmployed.checked) {
            if (employerName.value.trim() === '') {
                toastr.error("Please enter your employer's name.");
                isValid = false;
            }
            if (jobType.value.trim() === '') {
                toastr.error("Please enter your job type.");
                isValid = false;
            }
        }

        return isValid;
    }

    // Add validation to the Next button
    const nextButton = document.querySelector('button[step_number="4"]');
    nextButton.addEventListener('click', function(e) {
        if (!validateStep3()) {
            e.preventDefault();
        }
    });
});


/**
 * Validates the file fields on the application form.
 * Checks if all required file fields have been uploaded, and that the total size of the photograph files does not exceed 10 MB.
 * Displays error messages using the toastr library if any validation checks fail.
 * @returns {boolean} - True if all file field validations pass, false otherwise.
 */
function validateFileFields() {
    const fileFields = [
        { id: 'grade8', name: 'Grade 8 certificate' },
        { id: 'grade8t', name: 'Grade 9-12 transcript' },
        { id: 'E&Ecertificate', name: 'ESLCE AND EHEECE certificate' },
        { id: 'dgc', name: 'Diploma/degree certificate' },
        { id: 'dgc-student', name: 'Diploma/degree Student copy and Official transcript' },
        { id: 'student-sponsorship', name: 'Letter of sponsorship', optional: true },
        { id: 'Photograph', name: 'Photograph' }
    ];

    let isValid = true;

    fileFields.forEach(field => {
        const fileInput = document.getElementById(field.id);
        if (!fileInput.files.length && !field.optional) {
            toastr.error(`Please upload ${field.name}`);
            isValid = false;
        }
    });

    // Check if at least two photographs are uploaded
    const photographInput = document.getElementById('Photograph');
    if (photographInput.files.length < 2) {
        toastr.error('Please upload at least 2 photographs');
        isValid = false;
    }

    // Calculate total size of photograph files
    let totalSize = 0;
    for (let i = 0; i < photographInput.files.length; i++) {
        totalSize += photographInput.files[i].size;
    }

    // Check if total size of photographs exceeds 10 MB
    const maxSize = 10 * 1024 * 1024; // 10 MB in bytes
    if (totalSize > maxSize) {
        toastr.error('Total file size for Photographs should not exceed 10 MB');
        isValid = false;
    }

    return isValid;
}

const submitBtn = document.getElementById('btn-application');

const canvas = document.getElementById('signature-pad');
function clearCanvas() {
    console.log('Canvas cleared');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

submitBtn.addEventListener("click",async function(e) {
    e.preventDefault();
    let formData = {
        personalInformations: {},
        educationalBackground: {},
        employmentDetails: {},
        applicationDocuments: {},
        validationStatus: false
    };

    const fileToBase64 = (file) => new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result.split(',')[1]);
        reader.onerror = error => reject(error);
    });

    // Collect data for PersonalInformations
    formData.personalInformations = {
        username: document.getElementById('username').value,
        fatherName: document.getElementById('fathername').value,
        motherName: document.getElementById('mathername').value,
        citizenship: document.getElementById('citizenship').value,
        phoneNumber: document.getElementById('phonenumber').value,
        email: document.getElementById('useremail').value,
        sex: document.querySelector('input[name="sex"]:checked').value,
        placeOfBirth: document.getElementById('placeofbirth').value,
        dateOfBirth: document.getElementById('dob').value,
        isHandicapped: document.querySelector('input[name="handicap"]:checked').value,
        handicapExplanation: document.getElementById('handicapexplain').value,
        emergencyContactName: document.getElementById('closer_username').value,
        emergencyContactRelationship: document.getElementById('closer_relationship').value,
        emergencyContactAddress: document.getElementById('closer_address').value
    };

    // Collect data for EducationalBackground
    formData.educationalBackground = {
        hasPostSecondaryEducation: document.querySelector('input[name="post-secondary"]:checked').value === 'yes',
        postSecondaryInstitution: document.getElementById('diploma-explaination').value,
        secondarySchoolsAttended: document.getElementById('secondary-attended').value,
        eheeceResult: document.getElementById('eheece').value,
        eslceResult: document.getElementById('eslce').value,
        englishResult: document.getElementById('english').value,
        mathematicsResult: document.getElementById('mathematic').value,
        preparatoryStream: document.getElementById('preparatory-stream').value
    };

    // Collect data for EmploymentDetails
    formData.employmentDetails = {
        isCurrentlyEmployed: document.querySelector('input[name="employed"]:checked').value === 'yes',
        employerName: document.getElementById('employer-name').value,
        jobType: document.getElementById('job-type').value
    };

    // Collect data for ApplicationDocuments
    formData.applicationDocuments = {
        signature: document.getElementById('signature-pad').toDataURL().split(',')[1],
        grade8Certificate: await fileToBase64(document.getElementById('grade8').files[0]),
        grade912Transcript: await fileToBase64(document.getElementById('grade8t').files[0]),
        eslceEheeceCertificate: await fileToBase64(document.getElementById('E&Ecertificate').files[0]),
        diplomaDegreeCertificate: await fileToBase64(document.getElementById('dgc').files[0]),
        diplomaDegreeTranscript: await fileToBase64(document.getElementById('dgc-student').files[0]),
        sponsorshipLetter: await fileToBase64(document.getElementById('student-sponsorship').files[0]),
        photographs: await Promise.all(Array.from(document.getElementById('Photograph').files).map(fileToBase64))
    };

/**
     * Submits the application form data to the server using an AJAX request.
     *
     * @param {Object} formData - An object containing the form data to be submitted.
     * @returns {Promise<void>} - A Promise that resolves when the form data has been successfully submitted.
     */
    $.ajax({
        url: '/api/v1/applications/save',
        type: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        success: function (response) {
            $('#userAccountSetupForm')[0].reset();
            clearCanvas();
            toastr.success('Application form submitted successfully');
        },
        error: function (xhr, status, error) {
            console.log(xhr, status, error);
            toastr.error('Error submitting the application form', xhr);
        }
    });
        $.ajax({
        url: '/api/v1/applications/save',
        type: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        success: function (response) {
            $('#userAccountSetupForm')[0].reset();
            clearCanvas();
            // redirect to login page
            toastr.success('Application form submitted successfully');
        },
        error: function (xhr, status, error) {
            console.log(xhr, status, error);
            toastr.error('Error submitting the application form' , xhr);
        }
    });
});

