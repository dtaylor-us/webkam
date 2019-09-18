(ns ui.templates.user
  (:require [taoensso.timbre :as log]
            [express.web-api :refer [path-for]]))


(defn login-ui [csrf-token]
  [:div.content-container
   [:form.spacer-top-50 {:action "/user/authenticate", :method "post"}
    [:h2.center-text "Login"]
    [:input {:type "hidden", :name "_csrf", :value csrf-token}]
    [:div.mdc-text-field.mdc-text-field--box.email
     [:input.mdc-text-field__input {:type "email", :id "email-input", :name "email"}]
     [:label.mdc-floating-label {:for "email-input"} "Email"]
     [:div.mdc-line-ripple]]
    [:div.mdc-text-field.mdc-text-field--box.password
     [:input.mdc-text-field__input {:type "password", :id "password-input", :name "password" , :minlength "8"}]
     [:label.mdc-floating-label {:for "password-input"} "Password"]
     [:div.mdc-line-ripple]]
    [:div.form-button-container
     [:button.mdc-button.cancel {:type "button"} "Cancel"]
     [:button.mdc-button.mdc-button--raised.login {:type "submit"} "Login"]]]])

(defn register-ui [csrf-token]
  [:div.content-container
   [:form.spacer-top-50 {:action "/user/enroll", :method "post"}
    [:h2.center-text "Register"]
    [:input {:type "hidden", :name "_csrf", :value csrf-token}]
    [:div.mdc-text-field.mdc-text-field--box.username
     [:input {:type "text", :class "mdc-text-field__input", :id "username-input", :name "username" }]
     [:label.mdc-floating-label {:for "email-input"} "Username"]
     [:div.mdc-line-ripple]]
    [:div.mdc-text-field.mdc-text-field--box.email
     [:input.mdc-text-field__input {:type "email", :id "email-input", :name "email" }]
     [:label.mdc-floating-label {:for "email-input"} "Email"]
     [:div.mdc-line-ripple]]
    [:div.mdc-text-field.mdc-text-field--box.password
     [:input.mdc-text-field__input {:type "password", :id "password-input", :name "password", :minlength "8"}]
     [:label.mdc-floating-label {:for "password-input"} "Password"]
     [:div.mdc-line-ripple]]
    [:div.mdc-text-field.mdc-text-field--box.confirm-password
     [:input.mdc-text-field__input {:type "password", :id "confirm-password-input", :name "confirm-password", :minlength "8"}]
     [:label.mdc-floating-label {:for "confirm-password-input"} "Confirm Password"]
     [:div.mdc-line-ripple]]
    [:div.form-button-container
     [:button.mdc-button.cancel {:type "button"} "Cancel"]
     [:button.mdc-button.mdc-button--raised.login {:type "submit"} "Register"]]]])
