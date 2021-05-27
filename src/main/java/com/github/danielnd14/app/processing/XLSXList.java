package com.github.danielnd14.app.processing;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface XLSXList {
	List<Path> list() throws IOException;
}
