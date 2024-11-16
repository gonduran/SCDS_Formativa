export function openModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.style.display = "block";
}

export function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.style.display = "none";
}

export function addCloseOnOutsideClick(modalId) {
    const modal = document.getElementById(modalId);
    modal.addEventListener("click", (event) => {
        if (event.target === modal) {
            closeModal(modalId);
        }
    });
}