<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Реєстрація</title>
</head>
<body>

<h2>Реєстрація користувача</h2>

<form id="form">
    <input id="username"
           placeholder="Логін"
           required>

    <input id="password"
           type="password"
           placeholder="Пароль"
           required>

    <button type="submit">
        Зареєструватися
    </button>
</form>

<p id="result"></p>


<script>
document.getElementById("form")
    .addEventListener("submit", async function(event) {
        event.preventDefault();

        console.log("Form submitted!");

        const username =
            document.getElementById("username").value;

        const password =
            document.getElementById("password").value;

        console.log("Username:", username);

        try {
            const response = await fetch("/registration", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            console.log("HTTP Status:", response.status);

            const result = await response.text();
            console.log("Response:", result);

            document.getElementById("result").textContent =
                response.ok
                    ? "Реєстрація успішна"
                    : "Помилка " + response.status;

        } catch (error) {
            console.error("Request failed:", error);

            document.getElementById("result").textContent =
                "Помилка відправлення запиту";
        }
    });
</script>

</body>
</html>
