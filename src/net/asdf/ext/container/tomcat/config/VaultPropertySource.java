package net.asdf.ext.container.tomcat.config;

import java.util.Map;

import org.apache.tomcat.util.IntrospectionUtils.PropertySource;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;

/**
 * VAULT_ADDR
 * VAULT_TOKEN
 * VAULT_OPEN_TIMEOUT
 * VAULT_READ_TIMEOUT
 *
 * @author lemonfish
 *
 */
public class VaultPropertySource implements PropertySource{

	private Vault vault;

	public VaultPropertySource() {
		try {
			vault = new Vault(new VaultConfig().build());
		} catch (VaultException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getProperty(String key) {
		if(!key.startsWith("vault:")) {
			return null;
		}
		try {
			Map<String, String> data = vault.logical().read("secret/" + key.substring(6)).getData();
			return data.get("value");
		} catch (VaultException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(new VaultPropertySource().getProperty("vault:database/dev/shop-businesscard/url"));
	}

}
