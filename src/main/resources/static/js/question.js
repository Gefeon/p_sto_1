$(document).on('submit', '#askQuestionForm', function () {

    const ask_form = $(this);
    const questionDtoArray = ask_form.serializeToQuestionCreateDto();
    const form_data = JSON.stringify(questionDtoArray);

    if(validate(questionDtoArray)) {

        $.ajax({
            url: "/api/user/question",
            type: "POST",
            contentType: 'application/json',
            data: form_data,
            beforeSend: function (request) {
                let token = $.cookie("jwt_token");
                if (token != null) {
                    request.setRequestHeader("Authorization", "Bearer " + token);
                }
            },
            success: function (result) {
                let id = result.id;
                let persistDateTime = result.persistDateTime;
                let listTagDto = result.listTagDto;
                window.location.replace("/question");
            },
            error: function (error) {
                alert(error)
            }
        })
    }
    return false;
});

function validate(formData) {
    let isValidationsFails = false;
    let errorTitleFields = $("#titleError span");
    if (errorTitleFields.length >= 1) {
        errorTitleFields[0].remove();
    }
    let errorDescriptionFields = $("#descriptionError span");
    if (errorDescriptionFields.length >= 1) {
        errorDescriptionFields[0].remove();
    }
    let errorTagFields = $("#tagsError span");
    if (errorTagFields.length >= 1) {
        errorTagFields[0].remove();
    }
    if (formData.title === "") {
        $("#titleError")[0].insertAdjacentHTML('afterbegin',
            "<span style='color: red'>" +
                    "<b>Title cannot be empty!</b>" +
                "</span>");
        isValidationsFails = true;
    }
    if (formData.description === "") {
        $("#descriptionError")[0].insertAdjacentHTML('afterbegin',
            "<span style='color: red'>" +
            "<b>Description cannot be empty!</b>" +
            "</span>");
        isValidationsFails = true;
    }
    if (formData.tags.length === 0) {
        $("#tagsError")[0].insertAdjacentHTML('afterbegin',
            "<span style='color: red'>" +
            "<b>Tags cannot be empty!</b>" +
            "</span>");
        isValidationsFails = true;
    }
    if (formData.tags.length > 5) {
        $("#tagsError")[0].insertAdjacentHTML('afterbegin',
            "<span style='color: red'>" +
            "<b>Tag's amount cannot be more than 5!</b>" +
            "</span>");
        isValidationsFails = true;
    }
    return !isValidationsFails;
}

$.fn.serializeToQuestionCreateDto = function () {
    const questionCreateDto = {};
    const array = this.serializeArray();
    $.each(array, function () {
        if (questionCreateDto[this.name] !== undefined) {
            if (!questionCreateDto[this.name].push) {
                questionCreateDto[this.name] = [questionCreateDto[this.name]];
            }
            questionCreateDto[this.name].push(this.value || '');
        } else {
            if (this.name === "tags") {
                questionCreateDto[this.name] = this.value.split(/\s*[ ,;]\s*/).filter(item => item !== "").map((v) => ({"name": v}));
            } else {
                questionCreateDto[this.name] = this.value;
            }
        }
    });

    return questionCreateDto;
};
