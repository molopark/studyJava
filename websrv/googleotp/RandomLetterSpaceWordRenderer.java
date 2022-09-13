package com.molo.service.common.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.captcha.text.renderer.WordRenderer;

public class RandomLetterSpaceWordRenderer implements WordRenderer {

    private static final Random RAND = new SecureRandom();
	private static final List<Color> DEFAULT_COLORS = new ArrayList<Color>();
	private static final List<Font> DEFAULT_FONTS = new ArrayList<Font>();
	// The text will be rendered 25%/5% of the image height/width from the X and Y axes
	private static final double YOFFSET = 0.25;
	private static final double XOFFSET = 0.05;

    private final List<Color> _colors = new ArrayList<Color>();
    private final List<Font> _fonts = new ArrayList<Font>();

	static {
		DEFAULT_COLORS.add(new Color(173, 142, 219));
		/*
		DEFAULT_FONTS.add(new Font("Arial", Font.PLAIN, 19));
		DEFAULT_FONTS.add(new Font("Arial", Font.ITALIC, 19));
		DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 19));
		DEFAULT_FONTS.add(new Font("Arial", Font.PLAIN, 21));
		DEFAULT_FONTS.add(new Font("Arial", Font.ITALIC, 21));
		DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 21));
		DEFAULT_FONTS.add(new Font("Arial", Font.PLAIN, 23));
		DEFAULT_FONTS.add(new Font("Arial", Font.ITALIC, 23));
		DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 23));

		DEFAULT_FONTS.add(new Font("Courier", Font.PLAIN, 19));
		DEFAULT_FONTS.add(new Font("Courier", Font.ITALIC, 19));
		DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 19));
		DEFAULT_FONTS.add(new Font("Courier", Font.PLAIN, 21));
		DEFAULT_FONTS.add(new Font("Courier", Font.ITALIC, 21));
		DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 21));
		DEFAULT_FONTS.add(new Font("Courier", Font.PLAIN, 23));
		DEFAULT_FONTS.add(new Font("Courier", Font.ITALIC, 23));
		DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 23));
		*/
		DEFAULT_FONTS.add(new Font("Arial", Font.PLAIN, 22));
		DEFAULT_FONTS.add(new Font("Arial", Font.ITALIC, 22));
		DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 22));
		DEFAULT_FONTS.add(new Font("Arial", Font.PLAIN, 23));
		DEFAULT_FONTS.add(new Font("Arial", Font.ITALIC, 23));
		DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 23));
		DEFAULT_FONTS.add(new Font("Arial", Font.PLAIN, 25));
		DEFAULT_FONTS.add(new Font("Arial", Font.ITALIC, 25));
		DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 25));

		DEFAULT_FONTS.add(new Font("Courier", Font.PLAIN, 22));
		DEFAULT_FONTS.add(new Font("Courier", Font.ITALIC, 22));
		DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 22));
		DEFAULT_FONTS.add(new Font("Courier", Font.PLAIN, 23));
		DEFAULT_FONTS.add(new Font("Courier", Font.ITALIC, 23));
		DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 23));
		DEFAULT_FONTS.add(new Font("Courier", Font.PLAIN, 25));
		DEFAULT_FONTS.add(new Font("Courier", Font.ITALIC, 25));
		DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 25));
	}

    /**
     * Use the default color (black) and fonts (Arial and Courier).
     */
    public RandomLetterSpaceWordRenderer() {
    	this(DEFAULT_COLORS, DEFAULT_FONTS);
    }

    /**
     * Build a <code>WordRenderer</code> using the given <code>Color</code>s and
     * <code>Font</code>s.
     *
     * @param colors
     * @param fonts
     */
    public RandomLetterSpaceWordRenderer(List<Color> colors, List<Font> fonts) {
    	_colors.addAll(colors);
    	_fonts.addAll(fonts);
    }

    /**
     * Render a word onto a BufferedImage.
     *
     * @param word The word to be rendered.
     * @param image The BufferedImage onto which the word will be painted.
     */
    @Override
    public void render(final String word, BufferedImage image) {
        Graphics2D g = image.createGraphics();

		// 성능 및 사이즈 감소를 위해 옵션 변경
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED));
		g.setRenderingHints(hints);


        FontRenderContext frc = g.getFontRenderContext();
        int xBaseline = (int) Math.round(image.getWidth() * XOFFSET);
        int yBaseline =  image.getHeight() - (int) Math.round(image.getHeight() * YOFFSET);

        char[] chars = new char[1];
        for (char c : word.toCharArray()) {
            chars[0] = c;

            g.setColor(_colors.get(RAND.nextInt(_colors.size())));

            int choiceFont = RAND.nextInt(_fonts.size());
            Font font = _fonts.get(choiceFont);
            g.setFont(font);

            GlyphVector gv = font.createGlyphVector(frc, chars);
            g.drawChars(chars, 0, chars.length, xBaseline, yBaseline);

            int width = (int) gv.getVisualBounds().getWidth();
            xBaseline = xBaseline + width;

			// 문자 사이 간격 0~6 사이 랜덤으로 추가
			xBaseline += RAND.nextInt(6);
        }
    }

}
