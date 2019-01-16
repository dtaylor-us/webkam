// ES6
import {MDCRipple} from '@material/ripple';
import {MDCTextField} from '@material/textfield';
import {MDCTopAppBar} from "@material/top-app-bar";
import {MDCDrawer} from "@material/drawer";
import {MDCSnackbar} from '@material/snackbar';
import {MDCMenu} from '@material/menu';
import {MDCChipSet} from '@material/chips';


document.addEventListener("DOMContentLoaded", () => {

// global
    let topBarEl = document.getElementById('app-bar');
    if (topBarEl) {
        const topAppBar = new MDCTopAppBar(topBarEl);
        topAppBar.setScrollTarget(document.getElementById('main-content'));

        let drawerEl = document.querySelector('.mdc-drawer');
        if (drawerEl) {
            const drawer = MDCDrawer.attachTo(drawerEl);
            topAppBar.listen('MDCTopAppBar:nav', () => {
                drawer.open = !drawer.open;
            });
        }

    }

    console.log('Global Styles loaded');

// Communication

    let snackbarEl = document.querySelector('.mdc-snackbar');

    if (snackbarEl) {
        const snackbar = new MDCSnackbar(snackbarEl);

        if (flashes) {
            for (var flash of flashes) {
                const dataObj = {
                    message: flash,
                    actionText: 'Ok',
                    actionHandler: function () {
                        console.log(flash);
                    }
                };
                snackbar.show(dataObj);
                console.log("Snacking on Messages :-) :-)")
            }
        }

        if (validationErrors) {
            for (var valError of validationErrors) {
                const dataObj = {
                    message: valError,
                    actionText: 'Ok',
                    actionHandler: function () {
                        console.log(valError);
                    }
                };
                snackbar.show(dataObj);
                console.log("Snacking on Messages :-) :-)")
            }
        }
    }

// home
    let cancelEl = document.querySelector('.cancel');
    if (cancelEl) {
        new MDCRipple(cancelEl);
    }

    let fabRippleEl = document.querySelector('.mdc-fab');
    if (fabRippleEl) {
        const fabRipple = new MDCRipple(fabRippleEl);
        let menuEl = document.querySelector('.mdc-menu');
        if (menuEl) {
            const menu = new MDCMenu(menuEl);
            fabRipple.listen('click', () => {
                menu.open = !menu.open;
            });
        }
    }

    console.log('Starting load');

// login/register
    let usernameEl = document.querySelector('.username');
    if (usernameEl) {
        const username = new MDCTextField(usernameEl);
    }
    let emailEl = document.querySelector('.email');
    if (emailEl) {
        const email = new MDCTextField(emailEl);
    }
    let passwordEl = document.querySelector('.password');
    if (passwordEl) {
        const password = new MDCTextField(passwordEl);
    }
    let confirmPasswordEl = document.querySelector('.confirm-password');
    if (confirmPasswordEl) {
        const confirmPassword = new MDCTextField(confirmPasswordEl);
    }

    let registerEl = document.querySelector('.register');
    if (registerEl) {
        new MDCRipple(registerEl);
    }
    let loginEl = document.querySelector('.login');
    if (loginEl) {
        new MDCRipple(loginEl);
    }

    console.log('login/register page Styles loaded');
});

// CONTENT
let chipSetEl = document.querySelector('.mdc-chip-set');
if (chipSetEl) {
    const chipSet = new MDCChipSet(chipSetEl);
}

window.onbeforeunload = function () {
    window.scrollTo(0, 0);
};
