
let renderDate = (date) => {
    let d = new Date(date);
    return d.getDate() + '-'
        + d.getMonth() + '-'
        + d.getFullYear() + ' '
        + getHours(d.getHours()) + ':'
        + getMinutes(d.getMinutes()) + ' '
        + getDayOfWeekString(d.getDay());
};

let getDayOfWeekString = (dayOfWeek) => {
    if (dayOfWeek === 0) {
        return 'Sun';
    } else if (dayOfWeek === 1) {
        return 'Mon';
    } else if (dayOfWeek === 2) {
        return 'Tue';
    } else if (dayOfWeek === 3) {
        return 'Wed';
    } else if (dayOfWeek === 4) {
        return 'Thu'
    } else if (dayOfWeek === 5) {
        return 'Fri';
    } else if (dayOfWeek === 6) {
        return 'Sat';
    }
}

let getHours = (hours) => {
    if (hours < 10) {
        return '0' + hours;
    } else {
        return hours;
    }
}

let getMinutes = (minutes) => {
    if (minutes < 10) {
        return '0' + minutes;
    } else {
        return minutes;
    }
}

export default renderDate;
