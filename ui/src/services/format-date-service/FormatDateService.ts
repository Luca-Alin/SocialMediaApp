function formatDate(date: Date): string {
    date = new Date(date);

    const today = new Date();
    const currentDay = today.getDate();
    const currentMonth = today.getMonth();
    const currentYear = today.getFullYear();

    const inputDay = date.getDate();
    const inputMonth = date.getMonth();
    const inputYear = date.getFullYear();

    if (inputYear === currentYear && inputMonth === currentMonth && inputDay === currentDay) {
        // Date is today
        const hours = date.getHours().toString().padStart(2, "0");
        const minutes = date.getMinutes().toString().padStart(2, "0");
        return `${hours}:${minutes}`;
    } else if (inputYear === currentYear && inputMonth === currentMonth && inputDay >= currentDay - 7) {
        // Date is from this week
        const daysOfWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
        return daysOfWeek[date.getDay()].slice(0, 3);
    } else if (inputYear === currentYear && inputMonth === currentMonth && inputDay < currentDay - 7) {
        // Date is from the previous week
        const dayOfMonth = inputDay.toString();
        const monthName = new Intl.DateTimeFormat("en-US", {month: "long"}).format(date);
        return `${dayOfMonth} ${monthName}`;
    } else {
        // For other cases (e.g., dates from a month ago, last year, etc.), you can define your own logic here
        return date.toDateString(); // Default format for other cases
    }
}

export {formatDate};