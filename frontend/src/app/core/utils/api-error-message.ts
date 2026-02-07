export async function getApiErrorMessage(err: any): Promise<string> {
  const e = err?.error;

  // 1) backend retornou texto puro
  if (typeof e === 'string' && e.trim()) return e;

  // 2) backend: { timestamp, status, erro }
  if (e && typeof e === 'object') {
    if (typeof (e as any).erro === 'string' && (e as any).erro.trim()) {
      return (e as any).erro;
    }

    if (typeof (e as any).text === 'string' && (e as any).text.trim()) {
      return (e as any).text;
    }

    const msg =
      (e as any).message ||
      (e as any).detail ||
      (e as any).error ||
      (e as any).title ||
      (e as any).reason;

    if (typeof msg === 'string' && msg.trim()) return msg;
  }

  // 3) Blob
  if (typeof Blob !== 'undefined' && e instanceof Blob) {
    try {
      const text = await e.text();
      if (!text?.trim()) return 'Erro ao processar requisição.';

      // pode vir JSON em string
      try {
        const j = JSON.parse(text);
        return j.erro || j.message || j.detail || j.error || j.title || text;
      } catch {
        return text;
      }
    } catch {
      return 'Erro ao processar requisição.';
    }
  }

  // 4) fallback
  // Só usa se não tiver nada melhor
  return 'Erro ao processar requisição.';
}
