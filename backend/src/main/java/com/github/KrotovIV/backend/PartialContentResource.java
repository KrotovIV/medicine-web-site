package com.github.KrotovIV.backend;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public class PartialContentResource extends AbstractResource {

    private final Resource resource;
    private final long start;
    private final long end;

    public PartialContentResource(Resource resource, long start, long end) {
        this.resource = resource;
        this.start = start;
        this.end = end;
    }

    @Override
    public String getFilename() {
        return resource.getFilename();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = resource.getInputStream();
        is.skip(start);

        // Возвращаем только нужный диапазон
        return new InputStream() {
            private long bytesRead = 0;
            private long contentLength = end - start + 1;

            @Override
            public int read() throws IOException {
                if (bytesRead >= contentLength) {
                    return -1;
                }
                int result = is.read();
                if (result != -1) {
                    bytesRead++;
                }
                return result;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                if (bytesRead >= contentLength) {
                    return -1;
                }
                int maxRead = (int) Math.min(len, contentLength - bytesRead);
                int bytes = is.read(b, off, maxRead);
                if (bytes > 0) {
                    bytesRead += bytes;
                }
                return bytes;
            }

            @Override
            public void close() throws IOException {
                is.close();
            }
        };
    }

    @Override
    public long contentLength() throws IOException {
        return end - start + 1;
    }

    @Override
    public String getDescription() {
        return String.format("Partial content of %s [%d-%d]", resource.getDescription(), start, end);
    }
}