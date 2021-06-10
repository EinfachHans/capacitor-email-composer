import { registerPlugin } from '@capacitor/core';

import type { EmailComposerPlugin } from './definitions';

const EmailComposer = registerPlugin<EmailComposerPlugin>('EmailComposer', {
  web: () => import('./web').then(m => new m.EmailComposerWeb()),
});

export * from './definitions';
export { EmailComposer };
