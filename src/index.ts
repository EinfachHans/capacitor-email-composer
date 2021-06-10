import { registerPlugin } from '@capacitor/core';

import type { EmailComposerPlugin } from './definitions';

const EmailComposer = registerPlugin<EmailComposerPlugin>('EmailComposer');

export * from './definitions';
export { EmailComposer };
