document.addEventListener("DOMContentLoaded", () => {
    const images = document.querySelectorAll(".gallery-image");
    const modal = document.getElementById("imageModal");
    const modalImage = document.getElementById("modalImage");
    const closeButton = document.querySelector(".close");

    images.forEach((image) => {
        image.addEventListener("click", () => {
            modalImage.src = image.src;
            modal.style.display = "block";
        });
    });

    // Cerrar modal al hacer clic en la 'X'
    closeButton.addEventListener("click", () => {
        modal.style.display = "none";
    });

    // Cerrar modal al hacer clic fuera de la imagen
    modal.addEventListener("click", (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    });
});