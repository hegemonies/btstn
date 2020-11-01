
let baseApiUrl = window.location.href;

if (process.env.REACT_APP_ENV === "dev") {
    baseApiUrl = 'http://' + process.env.REACT_APP_API_URL
}

export default baseApiUrl;
