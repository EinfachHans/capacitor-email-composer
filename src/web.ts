import { WebPlugin } from '@capacitor/core';

import type { EmailComposerPlugin } from './definitions';

export class EmailComposerWeb extends WebPlugin implements EmailComposerPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
