export interface EmailComposerPlugin {
  /**
   * Checks if the User can send a Mail
   * iOS: Check if the current Device is configured to send mail
   * Android: Currently does nothing
   *
   * @since 1.0.0
   */
  hasAccount(): Promise<{hasAccount: boolean}>;

  /**
   * Open the E-Mail Composer
   *
   * @param options optional Options to prefill the E-Mail
   * @since 1.0.0
   */
  open(options?: OpenOptions): Promise<void>;
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
   * indicats if the body is HTML or plain text (primarily iOS)
   *
   * @since 1.0.1
   */
   isHtml?: boolean;
}
