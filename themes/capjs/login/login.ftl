<#import "template.ftl" as layout>
<@layout.registrationLayout>
    <form id="kc-form-login" onsubmit="return true;" action="${url.loginAction}" method="post">
        <div class="${properties.kcFormGroupClass!}">
            <label for="username" class="${properties.kcLabelClass!}">Username</label>
            <input id="username" name="username" type="text" class="${properties.kcInputClass!}" autofocus />
        </div>
        <div class="${properties.kcFormGroupClass!}">
            <label for="password" class="${properties.kcLabelClass!}">Password</label>
            <input id="password" name="password" type="password" class="${properties.kcInputClass!}" />
        </div>
        <div id="cap" data-cap-api-endpoint="/api"></div>
        <input type="hidden" name="cap-token" id="cap-token" />
        <div class="${properties.kcFormGroupClass!}">
            <input type="submit" value="Login" class="${properties.kcButtonClass!}" />
        </div>
    </form>
</@layout.registrationLayout>
