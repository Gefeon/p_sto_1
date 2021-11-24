
$(document).on('submit', '#loginForm', function(){

    const login_form = $(this);
    const form_data = JSON.stringify(login_form.serializeObject());

$.ajax({
    url: "/login",
    type: "POST",
    contentType: 'application/json',
    data: form_data,
    success: function (result) {

        //Get token in response body. If it will be in Set-Cookie header comment next 2 rows
        $.cookie("jwt", null, { path: '/' });
        $.cookie("jwt", result.jwt, {expires: 5, path: '/' });

        window.location.replace("/home");
    },
    error: function () {
        alert("error!")
    }
})
    return false;
});

$.fn.serializeObject = function(){

    const o = {};
    const a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
