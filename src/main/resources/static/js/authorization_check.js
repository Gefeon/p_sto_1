
const url = window. location. toString();

let jwt_token = document.cookie;
console.log(jwt_token);


fetch('http://localhost:8091/api/auth/token', {
    headers: {Authentication: 'Bearer'+ jwt_token}
})
    .then(resp => resp.json())
    .then( json => console.log(json))
