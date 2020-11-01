
let createNews = (id, message, date_timestamp, source) => {
    return {
        id: id,
        message: message,
        date: date_timestamp,
        source: source
    }
};

export default createNews;
