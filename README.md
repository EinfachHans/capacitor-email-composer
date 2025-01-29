# Capacitor E-Mail Composer

![Maintenance](https://img.shields.io/maintenance/yes/2025)
[![npm](https://img.shields.io/npm/v/capacitor-email-composer)](https://www.npmjs.com/package/capacitor-email-composer)

This Plugin is used to open a native E-Mail Composer within your Capacitor App.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Content**

- [Install](#install)
- [Attachments](#attachments)
  - [Device Storage](#device-storage)
  - [Native resources](#native-resources)
  - [Assets](#assets)
  - [Base64](#base64)
- [API](#api)
  - [hasAccount()](#hasaccount)
  - [open(...)](#open)
  - [Interfaces](#interfaces)
- [Changelog](#changelog)
- [Troubleshooting](#troubleshooting)
  - [TransactionTooLargeException](#transactiontoolargeexception)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Install

```bash
npm install capacitor-email-composer
npx cap sync
```

## Attachments

You can add attachments to the draft mail by using the `attachments` option in the `open(...)` method.
Every attachment needs a `type` and a `path`. If you are adding a `base64` type attachment, you also need to set the `name`:

### Device Storage

The path to the files must be defined absolute from the root of the file system. On Android the user has to allow the app first to read from external storage!

```ts
import { EmailComposer } from 'capacitor-email-composer'

EmailComposer.open({
  attachments: [{
    type: 'absolute',
    path: 'storage/sdcard/icon.png' // Android
  }]
})
```

### Native resources

Each app has a resource folder, e.g. the res folder for Android apps or the Resource folder for iOS apps. The following example shows how to attach the app icon from within the app's resource folder.

```ts
import { EmailComposer } from 'capacitor-email-composer'

EmailComposer.open({
  attachments: [{
    type: 'resource',
    path: 'icon.png'
  }]
})
```

### Assets

The path to the files must be defined relative from the root of the mobile web app assets folder, which is located under the build folder.

```ts
import { EmailComposer } from 'capacitor-email-composer'

EmailComposer.open({
  attachments: [{
    type: 'asset',
    path: '/icon/favicon.png' // starting slash is important
  }]
})
```

### Base64

The code below shows how to attach a base64 encoded image which will be added as an image. **You must set a name**.

```ts
import { EmailComposer } from 'capacitor-email-composer'

EmailComposer.open({
  attachments: [{
    type: 'base64',
    path: 'iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAYAAAA6...',
    name: 'icon.png' // this is required
  }]
})
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
hasAccount() => Promise<HasAccountResult>
```

Checks if the User can send a Mail
iOS: Check if the current Device is configured to send mail
Android: Currently does nothing

**Returns:** <code>Promise&lt;<a href="#hasaccountresult">HasAccountResult</a>&gt;</code>

**Since:** 1.0.0

--------------------


### open(...)

```typescript
open(options?: OpenOptions | undefined) => Promise<void>
```

Open the E-Mail Composer

| Param         | Type                                                | Description                            |
| ------------- | --------------------------------------------------- | -------------------------------------- |
| **`options`** | <code><a href="#openoptions">OpenOptions</a></code> | optional Options to prefill the E-Mail |

**Since:** 1.0.0

--------------------


### Interfaces


#### HasAccountResult

| Prop             | Type                 | Since |
| ---------------- | -------------------- | ----- |
| **`hasAccount`** | <code>boolean</code> | 1.0.0 |


#### OpenOptions

| Prop              | Type                      | Description                                                              | Since |
| ----------------- | ------------------------- | ------------------------------------------------------------------------ | ----- |
| **`to`**          | <code>string[]</code>     | email addresses for TO field                                             | 1.0.0 |
| **`cc`**          | <code>string[]</code>     | email addresses for CC field                                             | 1.0.0 |
| **`bcc`**         | <code>string[]</code>     | email addresses for BCC field                                            | 1.0.0 |
| **`subject`**     | <code>string</code>       | subject of the email                                                     | 1.0.0 |
| **`body`**        | <code>string</code>       | email body                                                               | 1.0.0 |
| **`isHtml`**      | <code>boolean</code>      | indicates if the body is HTML or plain text (primarily iOS)              | 1.0.1 |
| **`attachments`** | <code>Attachment[]</code> | attachments that are added to the mail file paths or base64 data streams | 1.2.0 |


#### Attachment

| Prop       | Type                                                         | Description                                                                                            | Since |
| ---------- | ------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------ | ----- |
| **`path`** | <code>string</code>                                          | The path of the attachment. See the docs for explained informations.                                   | 1.2.0 |
| **`type`** | <code>'absolute' \| 'resource' \| 'asset' \| 'base64'</code> | The type of the attachment. See the docs for explained informations.                                   | 1.2.0 |
| **`name`** | <code>string</code>                                          | The name of the attachment. See the docs for explained informations. Required for base64 attachements. | 1.2.0 |

</docgen-api>

## Changelog

The full Changelog is available [here](CHANGELOG.md)

## Troubleshooting

### TransactionTooLargeException

When sharing data between two applications, the Android OS might throw this exception for several reasons, for example if the file is too large.
Read more [here](https://github.com/EinfachHans/capacitor-email-composer/issues/19#issuecomment-1786087158) about how to work around this problem.
