function openTab(event, elementId) {
    let i, tabContent, tabLinks;

    /* get all elements by class, and hide them: */
    tabContent = document.getElementsByClassName("tabContent");
    for (i = 0; i < tabContent.length; i++) {
        tabContent[i].style.display = "none";
      }
    tabLinks = document.getElementsByClassName("tabLinks");
    for (i = 0; i < tabLinks.length; i++) {
        tabLinks[i].className = tabLinks[i].className.replace(" active", "");
      }
    document.getElementById(elementId).style.display = "flex";
    event.currentTarget.className += " active";
    
}