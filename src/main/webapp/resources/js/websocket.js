window.onload = () => {
    const ws = new WebSocket('ws://' + location.host + '/terminal')
        , term = new Terminal({ cursorBlink: false });

    ws.onopen = () => {
        term.open(document.getElementById('#terminal'), true);

        setTimeout(() => {
            ws.send('dir\r');
        }, 2000);
    };

    ws.onerror = e => { };
    ws.onclose = e => term.destroy();

    ws.onmessage = e => term.write(e.data);

    Object.assign(window, { ws });
}