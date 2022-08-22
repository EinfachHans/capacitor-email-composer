# Capacitor E-Mail Composer

![Maintenance](https://img.shields.io/maintenance/yes/2022)
[![npm](https://img.shields.io/npm/v/capacitor-email-composer)](https://www.npmjs.com/package/capacitor-email-composer)

This Plugin is used to open a native E-Mail Composer within your Capacitor App.

<!-- DONATE -->
[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG_global.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=LMX5TSQVMNMU6&source=url)

This and other Open-Source Cordova/Capacitor Plugins are developed in my free time.
To help ensure this plugin is kept updated, new features are added and bugfixes are implemented quickly, please donate a couple of dollars (or a little more if you can stretch) as this will help me to afford to dedicate time to its maintenance.
Please consider donating if you're using this plugin in an app that makes you money, if you're being paid to make the app, if you're asking for new features or priority bug fixes.
<!-- END DONATE -->

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Content**

- [Install](#install)
- [API](#api)
  - [hasAccount()](#hasaccount)
  - [open(...)](#open)
  - [Interfaces](#interfaces)
- [Changelog](#changelog)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Install

```bash
npm install capacitor-email-composer
npx cap sync
```

## API

<docgen-index>

* [`hasAccount()`](#hasaccount)
* [`open(...)`](#open)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### hasAccount()

```typescript
hasAccount() => Promise<{ hasAccount: boolean; }>
```

Checks if the User can send a Mail
iOS: Check if the current Device is configured to send mail
Android: Currently does nothing

**Returns:** <code>Promise&lt;{ hasAccount: boolean; }&gt;</code>

--------------------


### open(...)

```typescript
open(options?: OpenOptions | undefined) => Promise<void>
```

Open the E-Mail Composer

| Param         | Type                                                | Description                            |
| ------------- | --------------------------------------------------- | -------------------------------------- |
| **`options`** | <code><a href="#openoptions">OpenOptions</a></code> | optional Options to prefill the E-Mail |

--------------------


### Interfaces


#### OpenOptions

| Prop          | Type                  | Description                                                |
| ------------- | --------------------- | ---------------------------------------------------------- |
| **`to`**      | <code>string[]</code> | email addresses for TO field                               |
| **`cc`**      | <code>string[]</code> | email addresses for CC field                               |
| **`bcc`**     | <code>string[]</code> | email addresses for BCC field                              |
| **`subject`** | <code>string</code>   | subject of the email                                       |
| **`body`**    | <code>string</code>   | email body                                                 |
| **`isHtml`**  | <code>boolean</code>  | indicats if the body is HTML or plain text (primarily iOS) |

</docgen-api>

## Changelog

The full Changelog is available [here](CHANGELOG.md)
