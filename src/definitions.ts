export interface EmailComposerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
