const modal = document.getElementById("modal");
const modal2=document.getElementById("modal-plot");
const btn = document.getElementById("openBtn");
const openPlotBtn = document.getElementById("openPlotBtn");

function openModal() {
	modal.style.display = "flex";
}

function closeModal() {
	modal.style.display = "none";
}

function openModalPlot() {
	modal2.style.display = "flex";
	drawGraphs();
}

function closeModalPlot() {
	modal2.style.display = "none";
}

btn.onclick = openModal;
openPlotBtn.onclick = openModalPlot;

window.onclick = function(e) {
	if(e.target === modal) {
		closeModal();
	}
	if(e.target === modal2) {
			closeModalPlot();
		}
};