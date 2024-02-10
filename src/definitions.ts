export interface EmailComposerPlugin {
  /**
   * Checks if the User can send a Mail
   * iOS: Check if the current Device is configured to send mail
   * Android: Currently does nothing
   *
   * @since 1.0.0
   */
  hasAccount(): Promise<HasAccountResult>;

  /**
   * Open the E-Mail Composer
   *
   * @param options optional Options to prefill the E-Mail
   * @since 1.0.0
   */
  open(options?: OpenOptions): Promise<void>;
}

export interface HasAccountResult {
  /**
   * @since 1.0.0
   */
  hasAccount: boolean;
}

export interface OpenOptions {
  /**
   * email addresses for TO field
   *
   * @since 1.0.0
   */
  to?: string[];

  /**
   * email addresses for CC field
   *
   * @since 1.0.0
   */
  cc?: string[];

  /**
   * email addresses for BCC field
   *
   * @since 1.0.0
   */
  bcc?: string[];

  /**
   * subject of the email
   *
   * @since 1.0.0
   */
  subject?: string;

  /**
   * email body
   *
   * @since 1.0.0
   */
  body?: string;

  /**
   * indicates if the body is HTML or plain text (primarily iOS)
   *
   * @since 1.0.1
   */
  isHtml?: boolean;

  /**
   * attachments that are added to the mail
   * file paths or base64 data streams
   *
   * @since 1.2.0
   */
  attachments?: Attachment[];
}

export interface Attachment {
  /**
   * The path of the attachment. See the docs for explained informations.
   *
   * @since 1.2.0
   */
  path: string;

  /**
   * The type of the attachment. See the docs for explained informations.
   *
   * @since 1.2.0
   */
  type: 'absolute' | 'resource' | 'asset' | 'base64';

  /**
   * The name of the attachment. See the docs for explained informations.
   *
   * Required for base64 attachements.
   *
   * @since 1.2.0
   */
  name?: string;
}
