function loadSeats() {
    fetch('/admin/seatlist/json')
        .then(response => response.json())
        .then(data => {
            console.log(data);  // Logs the seat data from the server
            const seatTable = document.getElementById('seatTableBody');
            seatTable.innerHTML = '';  // Clear the table before populating

            // Loop through the seat data and add each row to the table
            data.forEach(seat => {
                seatTable.innerHTML += `
                    <tr>
                        <td>${seat.seatId}</td>
                        <td>${seat.section}</td>
                        <td>${seat.price} 원</td>  <!-- Display the price with the "원" currency -->
                    </tr>
                `;
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });
}